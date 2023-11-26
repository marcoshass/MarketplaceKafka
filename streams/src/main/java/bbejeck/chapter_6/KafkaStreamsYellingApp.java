package bbejeck.chapter_6;

import bbejeck.BaseStreamsApplication;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class KafkaStreamsYellingApp extends BaseStreamsApplication {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamsYellingApp.class);

    @Override
    public Topology topology(Properties streamProperties) {
        Serde<String> stringSerde = Serdes.String();
        StreamsBuilder builder = new StreamsBuilder();

        // Root node of the topology
        KStream<String, String> parent = builder.stream("src-topic",
                Consumed.with(Serdes.String(), Serdes.String()));

        // The parent-child relationship in Kafka Streams establishes the
        // direction for the flow of data, parent nodes forward records
        // to the child node(s).
        KStream<String, String> upperCasedStream = parent
                .mapValues(value -> value.toUpperCase());

        upperCasedStream.print(Printed.toSysOut());

        // The KStream.to method creates a processing node that writes the final
        // transformed records to a Kafka topic. It is a child of the
        // upperCasedStream, so it receives all of its inputs directly from the
        // results of the mapValues operation.
        upperCasedStream.to("out-topic",
                Produced.with(stringSerde, stringSerde));

        return builder.build(streamProperties);
    }

    public static void main(String[] args) throws Exception {
        KafkaStreamsYellingApp yellingApp = new KafkaStreamsYellingApp();
        var inputStream = yellingApp.getClass().getClassLoader().getResourceAsStream("local.properties");
        yellingApp.loadProperties(inputStream);

        Properties streamProperties = yellingApp.properties();
        Topology topology = yellingApp.topology(streamProperties);
        LOG.info("Topology description {}", topology.describe());

        CountDownLatch doneLatch = new CountDownLatch(1);
        try (KafkaStreams kafkaStreams = new KafkaStreams(topology, streamProperties)) {
            LOG.info("Hello World Yelling App Started");
            kafkaStreams.start();
            doneLatch.await(5, TimeUnit.MINUTES);
            LOG.info("Shutting down the Yelling APP now");
        }
    }
}
