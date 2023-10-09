package com.melik.analytics.service.transformer;

import com.melik.analytics.service.dataaccess.entity.AnalyticsEntity;
import com.melik.kafka.avro.model.TwitterAnalyticsAvroModel;
import org.springframework.stereotype.Component;
import org.springframework.util.IdGenerator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @Author mselvi
 * @Created 06.10.2023
 */

@Component
public class AvroToDbEntityModelTransformer {

    private final IdGenerator idGenerator;

    public AvroToDbEntityModelTransformer(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public List<AnalyticsEntity> getEntityModel(List<TwitterAnalyticsAvroModel> avroModels) {
        return avroModels.stream()
                .map(avroModel -> new AnalyticsEntity(
                        /*
                        * Burada id'yi kendimiz ürettik eğer db'de üretmeye çalışsaydık batch ile verileri ekleyemezdik.
                        * eğer db'de oluşturmaya çalışsaydık batch ile toplu insert yaparken elimizde id değeri olmayacaktı.
                        * */
                        idGenerator.generateId()
                        , avroModel.getWord()
                        , avroModel.getWordCount()
                        , LocalDateTime.ofInstant(Instant.ofEpochSecond(avroModel.getCreatedAt()), ZoneOffset.UTC)))
                .collect(toList());
    }


}
