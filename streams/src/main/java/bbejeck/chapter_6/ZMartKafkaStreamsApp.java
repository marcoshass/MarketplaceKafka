package bbejeck.chapter_6;

import bbejeck.BaseStreamsApplication;
import bbejeck.chapter_6.proto.RetailPurchaseProto.*;
import bbejeck.utils.SerdeUtil;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.protobuf.KafkaProtobufSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.*;

public class ZMartKafkaStreamsApp extends BaseStreamsApplication {
    private static final Logger LOG = LoggerFactory.getLogger(ZMartKafkaStreamsApp.class);
    private static final String CC_NUMBER_REPLACEMENT = "xxxx-xxxx-xxxx-";
    private boolean useSchemaRegistry = false;
    private static final String TRANSACTIONS = "transactions";
    private static final String PATTERNS = "patterns";
    private static final String REWARDS = "rewards";
    private static final String PURCHASES = "purchases";

    static final ValueMapper<RetailPurchase, RetailPurchase> creditCardMapper = retailPurchase -> {
        String[] parts = retailPurchase.getCreditCardNumber().split("-");
        String maskedCardNumber = CC_NUMBER_REPLACEMENT + parts[parts.length - 1];
        return RetailPurchase.newBuilder(retailPurchase).setCreditCardNumber(maskedCardNumber).build();
    };

    @Override
    public Topology topology(Properties streamProperties) {
        Serde<String> stringSerde = Serdes.String();
        Serde<RetailPurchase> retailPurchaseSerde;

        if (useSchemaRegistry) {
            final String schemaRegistryUrl = "http://localhost:8081";
            final Map<String, Object> configs =
                    Map.of(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);

            retailPurchaseSerde = new KafkaProtobufSerde<>(RetailPurchase.class);
        } else {
            retailPurchaseSerde = SerdeUtil.protobufSerde(RetailPurchase.class);
        }

        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, RetailPurchase> retailPurchaseKStream = streamsBuilder
                .stream(TRANSACTIONS, Consumed.with(stringSerde, retailPurchaseSerde))
                .mapValues(creditCardMapper);

        return streamsBuilder.build(streamProperties);
    }
}
