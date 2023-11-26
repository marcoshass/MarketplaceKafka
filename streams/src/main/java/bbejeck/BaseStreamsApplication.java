package bbejeck;

import org.apache.kafka.streams.kstream.ForeachAction;
import org.apache.kafka.streams.Topology;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class BaseStreamsApplication {
    final Properties properties = new Properties();

    public abstract Topology topology(final Properties properties);

    public void loadProperties(InputStream inputStream) throws IOException {
        properties.load(inputStream);
    }

    public Properties properties() {
        return properties;
    }

    public <K, V> ForeachAction<K, V> printKV(String label) {
        return (key, value) -> System.out.printf("%s: key[%s] value[%s] %n",label, key, value);
    }
}
