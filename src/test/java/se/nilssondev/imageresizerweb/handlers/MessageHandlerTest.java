package se.nilssondev.imageresizerweb.handlers;

import com.amazon.sqs.javamessaging.SQSConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.jms.JMSException;

import static org.junit.jupiter.api.Assertions.*;

class MessageHandlerTest {

    private SQSConnection connection;

    @Test
    void constructMessageHandler() throws JMSException, InterruptedException {
        MessageHandler messageHandler = new MessageHandler();
        assertTrue(messageHandler.getClient().queueExists("image-resizer-queue"));

    }

    @Test
    void testGetConnection() throws JMSException, InterruptedException {
        MessageHandler messageHandler = new MessageHandler();
        SQSConnection connection = messageHandler.getConnection();
        assertNotNull(connection);
        assertEquals(SQSConnection.class, connection.getClass());
        connection.stop();

    }

    @Test
    void testGetSession() throws JMSException, InterruptedException {
        MessageHandler messageHandler = new MessageHandler();
        messageHandler.createSession(connection);
        assertEquals("ImageEventListener", messageHandler.getConsumer().getMessageListener().getClass().getSimpleName());

    }

    @BeforeEach
    void setUp() throws JMSException, InterruptedException {
        this.connection = new MessageHandler().getConnection();
    }

    @AfterEach
    void tearDown() throws JMSException {
        connection.stop();
    }
}