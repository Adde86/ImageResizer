package se.nilssondev.imageresizerweb.handlers;

import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;
import se.nilssondev.imageresizerweb.implementations.ImageImplAWS;
import se.nilssondev.imageresizerweb.models.S3ObjectModel;
import se.nilssondev.imageresizerweb.services.ImageService;

import javax.jms.*;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@Component
@Slf4j
@Data
public class MessageHandler {

    private SQSConnection connection;
    private AmazonSQSMessagingClientWrapper client;
    private final String QUEUE_NAME = "image-resizer-queue";
    private MessageConsumer consumer;



    public MessageHandler() throws JMSException, InterruptedException {

        this.connection = createConnection();

         this.client = connection.getWrappedAmazonSQSClient();

        if(!client.queueExists(QUEUE_NAME)){
            client.createQueue(QUEUE_NAME);
        }

        createSession(connection);

        connection.start();

        Thread.sleep(1000);
    }

    public SQSConnection createConnection() throws JMSException {

        SQSConnectionFactory connectionFactory = new SQSConnectionFactory(
                new ProviderConfiguration(),
                AmazonSQSClientBuilder.standard().withRegion(Regions.EU_WEST_1)
        );
        return connectionFactory.createConnection();
    }

    public void createSession(SQSConnection connection) throws JMSException {

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_NAME);
        this.consumer = session.createConsumer(queue);
        this.consumer.setMessageListener(new ImageEventListener());

    }

}

@Slf4j
@Component
class ImageEventListener implements MessageListener {

    private ImageService imageService;
    private ImageHandler imageHandler;

    public ImageEventListener() {
        this.imageService = new ImageImplAWS();
        this.imageHandler = new ImageHandler();
    }

    @Override
    public void onMessage(Message message) {


        TextMessage text = (TextMessage) message;
        try {

            String body = text.getText();
            S3ObjectModel model = getS3ObjectModel(body);
            File fileToHandle = imageService.getImage(model.getKey(), "image-resizer-input-bucket");
            File processedFile = processImage(fileToHandle);
            if(imageService.save(processedFile, "image-resizer-output")) {
                fileToHandle.delete();
                processedFile.delete();
            }


        } catch (JMSException | IOException e) {
            log.error(e.getMessage());
        }
    }

    public File processImage(File fileToHandle) throws IOException {

       File updatedFile = imageHandler.resizeFile(fileToHandle);
       log.info("image: " +fileToHandle.getName() + " processed");
       return updatedFile;



    }

    public S3ObjectModel getS3ObjectModel(String text) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> json = mapper.readValue(text, Map.class);

        String messageString = json.get("Message");
        String listString = messageString.substring(messageString.indexOf(':')+1, messageString.length()-1).trim();
        List<Map<String,Object>> records = mapper.readValue(listString,List.class);
        Map<String,Object> firstRecord = records.get(0);
        Map<String, Object> s3 = (Map<String, Object>)firstRecord.get("s3");
        String bucketName = (String)((Map<String,Object>)s3.get("bucket")).get("name");
        String key = (String)((Map<String,Object>)s3.get("object")).get("key");


        return new S3ObjectModel(bucketName,"https://"+bucketName+".s3.amazonaws.com/"+key, key);
    }
}
