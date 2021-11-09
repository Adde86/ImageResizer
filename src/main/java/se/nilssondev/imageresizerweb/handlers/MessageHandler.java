package se.nilssondev.imageresizerweb.handlers;

import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.jms.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class MessageHandler {

    public MessageHandler() throws JMSException, InterruptedException {



        // Create a new connection factory with all defaults (credentials and region) set automatically
        SQSConnectionFactory connectionFactory = new SQSConnectionFactory(
                new ProviderConfiguration(),
                AmazonSQSClientBuilder.standard().withRegion(Regions.EU_WEST_1)
        );


// Create the connection.
        SQSConnection connection = connectionFactory.createConnection();
        AmazonSQSMessagingClientWrapper client = connection.getWrappedAmazonSQSClient();

        if(!client.queueExists("image-resizer-queue")){
            client.createQueue("image-resizer-queue");
        }

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("image-resizer-queue");
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(new ImageEventListener());

        connection.start();

        Thread.sleep(1000);
    }

}

class ImageEventListener implements MessageListener {

    @Override
    public void onMessage(Message message) {

        TextMessage text = (TextMessage) message;
        try {
            String body = text.getText();
            System.out.println(getURLFromMessage(body));
        } catch (JMSException  | JsonProcessingException e) {
            e.printStackTrace();

        }
    }
    public String getURLFromMessage(String text) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> json = mapper.readValue(text, Map.class);

        String messageString = json.get("Message");
        String listString = messageString.substring(messageString.indexOf(':')+1, messageString.length()-1).trim();
        List<Map<String,Object>> records = mapper.readValue(listString,List.class);
        Map<String,Object> firstRecord = records.get(0);
        Map<String, Object> s3 = (Map<String, Object>)firstRecord.get("s3");
        String bucketName = (String)((Map<String,Object>)s3.get("bucket")).get("name");
        String id = (String)((Map<String,Object>)s3.get("object")).get("key");

        return "https://"+bucketName+".s3.amazonaws.com/"+id;
    }
}
