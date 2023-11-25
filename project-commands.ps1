First let’s open a new terminal window and start a shell in the broker container:

docker exec -it broker bash

# Create a topic
kafka-topics --create --topic first-topic\
--bootstrap-server localhost:9092\
--replication-factor 1\
--partitions 1

# Kafka Producer
kafka-console-producer --topic second-topic \
--broker-list localhost:9092 \
--property parse.key=true \
--property key.separator=":"

#3 Specifying that you’ll provide a key
#4 Specifying the separator of the key and value


# Kafka Consumer
kafka-console-consumer --topic purchases \
--bootstrap-server broker:9092 \
--property print.key=true \
--property key.separator="-" \
--partition 0

kafka-console-consumer --topic second-topic \
--bootstrap-server broker:9092 \
--property print.key=true \
--property key.separator="-" \
--partition 1

kafka-console-consumer --topic second-topic \
--bootstrap-server broker:9092 \
--property print.key=true \
--property key.separator="-" \
--partition 2



# <String,String> Producer
kafka-console-producer --broker-list broker:9092 --topic streams-plaintext-input

# <String,String> Consumer
kafka-console-consumer --bootstrap-server broker:9092 \
 --topic streams-wordcount-output \
 --from-beginning \
 --formatter kafka.tools.DefaultMessageFormatter \
 --property print.key=true \
 --property print.value=true \
 --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
 --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer


# <String,Long> Consumer
 kafka-console-consumer --bootstrap-server broker:9092 \
 --topic avro-avengers \
 --formatter kafka.tools.DefaultMessageFormatter \
 --property print.key=true \
 --property print.value=true \
 --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
 --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer



 ################ Favourite Color
 kafka-console-producer --broker-list broker:9092 --topic favourite-color-input

 # <String,String> Consumer
 kafka-console-consumer --bootstrap-server broker:9092 \
 --topic out-topic \
 --from-beginning \
 --formatter kafka.tools.DefaultMessageFormatter \
 --property print.key=true \
 --property print.value=true \
 --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
 --property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer

  # <String,Long> Consumer
 kafka-console-consumer --bootstrap-server broker:9092 \
 --topic favourite-color-output \
 --formatter kafka.tools.DefaultMessageFormatter \
 --property print.key=true \
 --property print.value=true \
 --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
 --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer



 #Upload a schema
 jq '. | {schema: tojson}' src/main/avro/avenger.avsc | \ #1
curl -s -X
POST http://localhost:8081/subjects/avro-avengers-value/versions\ #2
-H "Content-Type: application/vnd.schemaregistry.v1+json" \ #3
-d @- \ #4
| jq #5


# list all the subjects of registered
# schemas with the following command:
curl -s "http://localhost:8081/subjects" | jq

# The next command shows you all of versions for a given schema
curl -s "http://localhost:8081/subjects/avro-avengers-value/versions" | jq

# retrieve the schema at a specific version
curl -s "http://localhost:8081/subjects/avro-avengers-value/versions/1" \
| jq '.'

# if you don’t care about previous versions of a schema and you only
# want the latest one, you don’t need to know the actual latest version number. You can
# use the following REST API call to retrieve the latest schema:
$ curl -s "http://localhost:8081/subjects/avro-avengers-value/versions/latest" | jq '.'

# Generating code from the schema makes your life as developer easier, as it
# automates the repetitive, boilerplate process of creating domain objects.
# 															^^^^^^^^^^^^^^

# Register schemas and generate Java files
# ./gradlew streams:registerSchemasTask
# ./gradlew clean build #2

# Create some domain objects from the generated Java files
#             ^^^^^^^^^^^^^^
