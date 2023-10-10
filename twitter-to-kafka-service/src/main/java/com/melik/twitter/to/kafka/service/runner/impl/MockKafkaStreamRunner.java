package com.melik.twitter.to.kafka.service.runner.impl;

import com.melik.app.config.data.config.TwitterToKafkaServiceConfigData;
import com.melik.twitter.to.kafka.service.exception.TwitterToKafkaServiceException;
import com.melik.twitter.to.kafka.service.listener.TwitterKafkaStatusListener;
import com.melik.twitter.to.kafka.service.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author mselvi
 * @Created 25.09.2023
 */

@Component
@ConditionalOnProperty(name = "twitter-to-kafka-service.enable-mock-tweets", havingValue = "true")
public class MockKafkaStreamRunner implements StreamRunner {

    private static final Logger LOG = LoggerFactory.getLogger(MockKafkaStreamRunner.class);
    private static final Random RANDOM = new Random();
    private static final String[] WORDS = new String[]{
            "Lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing", "elit", "Etiam", "at", "libero", "nulla", "Ut", "sollicitudin"
    };
    private static final String tweetAsRawJson = "{" +
            "\"created_at\":\"{0}\"," +
            "\"id\":\"{1}\"," +
            "\"text\":\"{2}\"," +
            "\"user\":{\"id\":\"{3}\"}" +
            "}";
    private static final String TWITTER_STATUS_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";

    private final TwitterToKafkaServiceConfigData configData;
    private final TwitterKafkaStatusListener statusListener;

    public MockKafkaStreamRunner(TwitterToKafkaServiceConfigData configData, TwitterKafkaStatusListener statusListener) {
        this.configData = configData;
        this.statusListener = statusListener;
    }

    @Override
    public void start() throws TwitterException {
        String[] keywords = configData.getTwitterKeywords().toArray(new String[]{});
        int mockMinTweetLenght = configData.getMockMinTweetLength();
        int mockMaxTweetLenght = configData.getMockMaxTweetLength();
        long mockSleepMs = configData.getMockSleepMs();
        LOG.info("Starting mock filtering twitter streams for keywords {}", Arrays.toString(keywords));
        simulateTwitterStream(keywords, mockMaxTweetLenght, mockMinTweetLenght, mockSleepMs);
    }

    private void simulateTwitterStream(String[] keywords, int mockMaxTweetLenght, int mockMinTweetLenght, long mockSleepMs) {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                while (true) {
                    String formattedTweetAsRawJson = getFormattedTweet(keywords, mockMinTweetLenght, mockMaxTweetLenght);
                    Status status = TwitterObjectFactory.createStatus(formattedTweetAsRawJson);
                    statusListener.onStatus(status);
                    sleepBetweenTweets(mockSleepMs);
                }
            } catch (TwitterException exception) {
                LOG.error("Error creating twitter status", exception);
            }
        });
    }

    private String getFormattedTweet(String[] keywords, int mockMinTweetLenght, int mockMaxTweetLenght) {
        String[] params = new String[]{
                ZonedDateTime.now().format(DateTimeFormatter.ofPattern(TWITTER_STATUS_DATE_FORMAT, Locale.ENGLISH)),
                String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE)),
                getRandomTweetContent(keywords, mockMinTweetLenght, mockMaxTweetLenght),
                String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE))
        };
        return formatTweetAsJsonWithParams(params);
    }

    private String getRandomTweetContent(String[] keywords, int mockMinTweetLenght, int mockMaxTweetLenght) {
        StringBuilder content = new StringBuilder();
        int contentLenght = RANDOM.nextInt((mockMaxTweetLenght - mockMinTweetLenght + 1) + mockMinTweetLenght);
        return constructRandomContent(keywords, content, contentLenght);
    }

    private String constructRandomContent(String[] keywords, StringBuilder content, int contentLenght) {
        for (int i = 0; i < contentLenght; i++) {
            content.append(WORDS[RANDOM.nextInt(WORDS.length)]).append(" ");
            if (i == contentLenght / 2) {
                content.append(keywords[RANDOM.nextInt(keywords.length)]).append(" ");
            }
        }
        return content.toString().trim();
    }

    private String formatTweetAsJsonWithParams(String[] params) {
        String tweet = tweetAsRawJson;
        for (int i = 0; i < params.length; i++) {
            tweet = tweet.replace("{" + i + "}", params[i]);
        }
        return tweet;
    }

    private void sleepBetweenTweets(long mockSleepMs) {
        try {
            Thread.sleep(mockSleepMs);
        } catch (InterruptedException e) {
            throw new TwitterToKafkaServiceException("Error while sleeping for waiting new status to create");
        }
    }


}
