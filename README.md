#### Real-Time Event-Driven Inventory Management System using Spring Boot, Kafka, Kafka Streams and Avro Serialization.



A real-time, event-driven inventory management system that processes product updates as streaming events using Apache Kafka.

The system implements an event-driven architecture where every inventory action—create, sell, restock, update, and delete is captured as an event and processed in real time using Kafka Streams.

It leverages hourly windowed aggregations and stateful stream processing to maintain live inventory levels, detect low stock conditions, and generate business insights such as top-selling products. Processed data is persisted into PostgreSQL for downstream querying and reporting.

