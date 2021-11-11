package se.nilssondev.imageresizerweb.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import se.nilssondev.imageresizerweb.models.S3ObjectModel;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ImageEventListenerTest {


    String message;

    private static LogCaptor logCaptor;

    ImageEventListener eventListener;

    @Test
    void testMessage() throws JMSException, InterruptedException, JsonProcessingException {


        S3ObjectModel model = eventListener.getS3ObjectModel(message);
        String expectedUrl = "https://image-resizer-images.s3.amazonaws.com/"+"images/1194786_527386_635904744488623840.jpg";
        String expectedBucketName = "image-resizer-images";
        String expectedKey = "images/1194786_527386_635904744488623840.jpg";
        assertEquals(expectedUrl, model.getUrl());
        assertEquals(expectedBucketName,model.getBucketName());
        assertEquals(expectedKey, model.getKey());
    }

   @Test
   void resizeImageTest() throws IOException {
        File file = new File("src/main/resources/static/arsta.jpg");

        File newFile = eventListener.processImage(file);
        assertTrue(Files.exists(Path.of("src/main/resources/static/arsta_thumb.jpg")));

        newFile.delete();
   }

    @AfterEach
    void tearDown() {
        logCaptor.clearLogs();
    }

    @BeforeAll
    static void beforeAll() {
        logCaptor = LogCaptor.forClass(ImageHandler.class);
    }

    @BeforeEach
    void setUp() {
        eventListener = new ImageEventListener();
        message = "{\n" +
                "  \"Type\" : \"Notification\",\n" +
                "  \"MessageId\" : \"364bf636-332b-51a3-a3fe-ac3506645279\",\n" +
                "  \"TopicArn\" : \"arn:aws:sns:eu-west-1:977629633660:image-uploaded\",\n" +
                "  \"Subject\" : \"Amazon S3 Notification\",\n" +
                "  \"Message\" : \"{\\\"Records\\\":[{\\\"eventVersion\\\":\\\"2.1\\\",\\\"eventSource\\\":\\\"aws:s3\\\",\\\"awsRegion\\\":\\\"eu-west-1\\\",\\\"eventTime\\\":\\\"2021-11-08T14:24:13.786Z\\\",\\\"eventName\\\":\\\"ObjectCreated:Put\\\",\\\"userIdentity\\\":{\\\"principalId\\\":\\\"AWS:AIDA6HH2ELR6FLRKZPDRT\\\"},\\\"requestParameters\\\":{\\\"sourceIPAddress\\\":\\\"83.250.50.237\\\"},\\\"responseElements\\\":{\\\"x-amz-request-id\\\":\\\"JESD39B3K59K0MMM\\\",\\\"x-amz-id-2\\\":\\\"jrLUGV/vbu8DMRNzGNm1Halb1GHjPJk2HfF9NtY7jmouMfq1HfOIrF8JEpKVfLlrChgHZXeF4mvEtAxnB4yx9GWWo7p2wCDM\\\"},\\\"s3\\\":{\\\"s3SchemaVersion\\\":\\\"1.0\\\",\\\"configurationId\\\":\\\"image-uploaded\\\",\\\"bucket\\\":{\\\"name\\\":\\\"image-resizer-images\\\",\\\"ownerIdentity\\\":{\\\"principalId\\\":\\\"A37DYUGILQ74GA\\\"},\\\"arn\\\":\\\"arn:aws:s3:::image-resizer-images\\\"},\\\"object\\\":{\\\"key\\\":\\\"images/1194786_527386_635904744488623840.jpg\\\",\\\"size\\\":85922,\\\"eTag\\\":\\\"580560d0d6d70bacfa331380530d2f73\\\",\\\"sequencer\\\":\\\"006189330DB73C3508\\\"}}}]}\",\n" +
                "  \"Timestamp\" : \"2021-11-08T14:24:15.297Z\",\n" +
                "  \"SignatureVersion\" : \"1\",\n" +
                "  \"Signature\" : \"o768cFJ0RZM4j978p+x/ryQci6LM7MEeRrwDN7hVppOvkcgbD3sIkRRIRJHu0wr0qSDUOgKyMmhQWXZSoPqunUrBDRZU6PgKbYRljOSyrqeZJMX8HZudr/1fmYOiaELmjb7zytNVeCQJU3ZxIidmlnbTA2qU8BgKRgtQBMkJ38sJ6P11CTeTsljQUzxBrC3d7hf0Ej5KghZ9Cly8oPJcnNQfs13McmbsTsUwRTdAlXcFkJeG2mptZhzfyD4mZc0VURK30U3qV2unTsAcNNUSgHns1DhB7Vfk4ZEop89sEB0o1GmVyQs6PPJDz5/0nERcvgTIBzWtzcGvZQYAhxrpYg==\",\n" +
                "  \"SigningCertURL\" : \"https://sns.eu-west-1.amazonaws.com/SimpleNotificationService-7ff5318490ec183fbaddaa2a969abfda.pem\",\n" +
                "  \"UnsubscribeURL\" : \"https://sns.eu-west-1.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:eu-west-1:977629633660:image-uploaded:34189a95-5437-4167-a880-1376ab093894\"\n" +
                "}";
    }
}

class TestTextMessage implements TextMessage {

    private String text;

    @Override
    public void setText(String s) throws JMSException {
        this.text = s;
    }

    @Override
    public String getText() throws JMSException {
        return this.text;
    }

    @Override
    public String getJMSMessageID() throws JMSException {
        return null;
    }

    @Override
    public void setJMSMessageID(String s) throws JMSException {

    }

    @Override
    public long getJMSTimestamp() throws JMSException {
        return 0;
    }

    @Override
    public void setJMSTimestamp(long l) throws JMSException {

    }

    @Override
    public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
        return new byte[0];
    }

    @Override
    public void setJMSCorrelationIDAsBytes(byte[] bytes) throws JMSException {

    }

    @Override
    public void setJMSCorrelationID(String s) throws JMSException {

    }

    @Override
    public String getJMSCorrelationID() throws JMSException {
        return null;
    }

    @Override
    public Destination getJMSReplyTo() throws JMSException {
        return null;
    }

    @Override
    public void setJMSReplyTo(Destination destination) throws JMSException {

    }

    @Override
    public Destination getJMSDestination() throws JMSException {
        return null;
    }

    @Override
    public void setJMSDestination(Destination destination) throws JMSException {

    }

    @Override
    public int getJMSDeliveryMode() throws JMSException {
        return 0;
    }

    @Override
    public void setJMSDeliveryMode(int i) throws JMSException {

    }

    @Override
    public boolean getJMSRedelivered() throws JMSException {
        return false;
    }

    @Override
    public void setJMSRedelivered(boolean b) throws JMSException {

    }

    @Override
    public String getJMSType() throws JMSException {
        return null;
    }

    @Override
    public void setJMSType(String s) throws JMSException {

    }

    @Override
    public long getJMSExpiration() throws JMSException {
        return 0;
    }

    @Override
    public void setJMSExpiration(long l) throws JMSException {

    }

    @Override
    public int getJMSPriority() throws JMSException {
        return 0;
    }

    @Override
    public void setJMSPriority(int i) throws JMSException {

    }

    @Override
    public void clearProperties() throws JMSException {

    }

    @Override
    public boolean propertyExists(String s) throws JMSException {
        return false;
    }

    @Override
    public boolean getBooleanProperty(String s) throws JMSException {
        return false;
    }

    @Override
    public byte getByteProperty(String s) throws JMSException {
        return 0;
    }

    @Override
    public short getShortProperty(String s) throws JMSException {
        return 0;
    }

    @Override
    public int getIntProperty(String s) throws JMSException {
        return 0;
    }

    @Override
    public long getLongProperty(String s) throws JMSException {
        return 0;
    }

    @Override
    public float getFloatProperty(String s) throws JMSException {
        return 0;
    }

    @Override
    public double getDoubleProperty(String s) throws JMSException {
        return 0;
    }

    @Override
    public String getStringProperty(String s) throws JMSException {
        return null;
    }

    @Override
    public Object getObjectProperty(String s) throws JMSException {
        return null;
    }

    @Override
    public Enumeration getPropertyNames() throws JMSException {
        return null;
    }

    @Override
    public void setBooleanProperty(String s, boolean b) throws JMSException {

    }

    @Override
    public void setByteProperty(String s, byte b) throws JMSException {

    }

    @Override
    public void setShortProperty(String s, short i) throws JMSException {

    }

    @Override
    public void setIntProperty(String s, int i) throws JMSException {

    }

    @Override
    public void setLongProperty(String s, long l) throws JMSException {

    }

    @Override
    public void setFloatProperty(String s, float v) throws JMSException {

    }

    @Override
    public void setDoubleProperty(String s, double v) throws JMSException {

    }

    @Override
    public void setStringProperty(String s, String s1) throws JMSException {

    }

    @Override
    public void setObjectProperty(String s, Object o) throws JMSException {

    }

    @Override
    public void acknowledge() throws JMSException {

    }

    @Override
    public void clearBody() throws JMSException {

    }
}