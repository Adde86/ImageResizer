package se.nilssondev.imageresizerweb.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import javax.jms.JMSException;
import javax.jms.Message;

import static org.junit.jupiter.api.Assertions.*;

class ImageEventListenerTest {

    @Test
    void testMessage() throws JMSException, InterruptedException, JsonProcessingException {
        String message = "{\n" +
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

        ImageEventListener eventListener = new ImageEventListener();

        String url = eventListener.getURLFromMessage(message);
        String expected = "https://image-resizer-images.s3.amazonaws.com/"+"images/1194786_527386_635904744488623840.jpg";
        assertEquals(expected, url);
    }

}