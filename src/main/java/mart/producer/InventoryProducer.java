package mart.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import mart.avro.InventoryEvent;

@Service
public class InventoryProducer {

    private static final String TOPIC = "inventory-events";

    private final KafkaTemplate<String, InventoryEvent> kafkaTemplate;

    public InventoryProducer(KafkaTemplate<String, InventoryEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(InventoryEvent event) {
        kafkaTemplate.send(
                TOPIC,
                event.getInventoryId().toString(),
                event
        );
    }
}