package io.dongvelop.catalogservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dongvelop.catalogservice.jpa.CatalogEntity;
import io.dongvelop.catalogservice.jpa.CatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 10. 23
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final CatalogRepository catalogRepository;

    @KafkaListener(topics = "example-catalog-topic")
    public void updateQty(final String kafkaMessage) {
        log.info("kafkaMessage[{}]", kafkaMessage);

        final ObjectMapper mapper = new ObjectMapper();
        HashMap<Object, Object> map = new HashMap<>();

        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<>() {
            });
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("", jsonProcessingException);
        }

        final CatalogEntity catalog = catalogRepository.findByProductId(map.get("productId").toString());
        log.info("catalog[{}]", catalog);

        if (catalog != null) {
            catalog.setStock(catalog.getStock() - (Integer) map.get("qty"));
            catalogRepository.save(catalog);
        }
    }
}
