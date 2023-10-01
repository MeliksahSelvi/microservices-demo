package com.melik.twitter.to.kafka.service.runner;

import twitter4j.TwitterException;

/**
 * @Author mselvi
 * @Created 25.09.2023
 */

public interface StreamRunner {
    void start() throws TwitterException;
}
