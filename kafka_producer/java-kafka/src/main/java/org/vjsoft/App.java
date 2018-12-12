package org.vjsoft;

import java.io.*;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;



/**
 * Hello world!
 *
 */
public class App 
{

    /*
    public interface IKafkaConstants {
        public static String KAFKA_BROKERS = "localhost:9092";
        public static Integer MESSAGE_COUNT=1000;
        public static String CLIENT_ID="client1";
        public static String TOPIC_NAME="demo";
        public static String GROUP_ID_CONFIG="consumerGroup1";
        public static Integer MAX_NO_MESSAGE_FOUND_COUNT=100;
        public static String OFFSET_RESET_LATEST="latest";
        public static String OFFSET_RESET_EARLIER="earliest";
        public static Integer MAX_POLL_RECORDS=1;
    }
*/

   // public class ProducerCreator {
        public static Producer<Long, String> createProducer() {
            Properties props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            props.put(ProducerConfig.CLIENT_ID_CONFIG, "client1");
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.LongSerializer");
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
            props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "test-transactional-id");
            //props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class.getName());
            return new KafkaProducer<>(props);
        }
    //}


    public static void main( String[] args )
    {
        System.out.println( "Hello Kafka producers!" );


        Producer<Long, String> producer = createProducer();
        File file = new File("/Users/varunjajee/Data/SampleData/clickstream-sample.json");


        long i=1;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            producer.initTransactions();

            String st;
            while ((st = br.readLine()) != null) {

                System.out.println(st);
                producer.beginTransaction();

                producer.send(new ProducerRecord<>("mytopic", i, st));

                producer.commitTransaction();
                i++;

                Thread.sleep(2000);
            }


            producer.close();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {

        }
    }
}
