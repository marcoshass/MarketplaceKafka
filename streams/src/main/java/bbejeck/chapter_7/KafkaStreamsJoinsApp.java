package bbejeck.chapter_7;

import bbejeck.BaseStreamsApplication;
import bbejeck.chapter_6.proto.RetailPurchaseProto.RetailPurchase;
import bbejeck.chapter_7.proto.CoffeePurchaseProto.CoffeePurchase;
import bbejeck.chapter_8.proto.PromotionProto.Promotion;
import bbejeck.utils.SerdeUtil;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import java.util.Properties;
import org.slf4j.*;

// You considered each transaction in isolation, without any
// regard to other events occurring at the same time or within certain time boundaries,
// either before or after the transaction. Also, "you only dealt with individual streams,
// ignoring any possibility of gaining additional insight by joining streams together."
//
// You can utilize state in different ways. We’ll look at one example
// when we explore the stateful operations, such as the accumulation of values, provided
// by the Kafka Streams DSL.
//
//
public class KafkaStreamsJoinsApp extends BaseStreamsApplication {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamsJoinsApp.class);

    @Override
    public Topology topology(Properties properties) {
        StreamsBuilder builder = new StreamsBuilder();
        Serde<CoffeePurchase> coffeSerde = SerdeUtil.protobufSerde(CoffeePurchase.class);
        Serde<RetailPurchase> retailSerde = SerdeUtil.protobufSerde(RetailPurchase.class);
        Serde<Promotion> promotionSerde = SerdeUtil.protobufSerde(Promotion.class);
        Serde<String> stringSerde = Serdes.String();
        return null;
    }

    public static void main(String[] args) {

    }
}
