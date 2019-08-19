package jsonMain;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;

import com.ibm.mq.jms.JMSC;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.mq.jms.MQTopicConnectionFactory;

public class MQTopicSenderClient {
    public void goMQ(String data) {
        try {
            /*MQ Configuration*/
//        	InitialContext ic = new InitialContext(); // or as appropraite
  //      	ConnectionFactory cf = (ConnectionFactory)ic.lookup("myMQfactory"); // replace with JNDI name
        	
        	MQTopicConnectionFactory mqTopicConnectionFactory = new MQTopicConnectionFactory();
        	mqTopicConnectionFactory.setHostName("localhost");
        	mqTopicConnectionFactory.setChannel("DEV.ADMIN.SVRCONN");//communications link
        	mqTopicConnectionFactory.setPort(1414);
        	mqTopicConnectionFactory.setQueueManager("QM1");//service provider 
        	mqTopicConnectionFactory.setTransportType(JMSC.MQJMS_TP_CLIENT_MQ_TCPIP);
        //    mqQueueConnectionFactory.set
            
            /*Create Connection */
            TopicConnection topicConnection = mqTopicConnectionFactory.createTopicConnection("admin","passw0rd");
            
            topicConnection.start();
            

            /*Create session */
            TopicSession topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            /*Create response queue */
       //     Queue queue = queueSession.createQueue("Queue2");
            Topic topic=topicSession.createTopic("DEV.TOPIC");

            /*Create text message */
            TextMessage textMessage = topicSession.createTextMessage("hello world");
       //     textMessage.setJMSReplyTo(queue);
         //   textMessage.setJMSType("mcd://xmlns");//message type
          //  textMessage.setJMSExpiration(2*1000);//message expiration
          //  textMessage.setJMSDeliveryMode(DeliveryMode.PERSISTENT); //message delivery mode either persistent or non-persistemnt

            /*Create sender queue */
//            QueueSender queueSender = queueSession.createSender(queueSession.createQueue("Queue1"));
            MessageProducer mqProducer=topicSession.createProducer(topicSession.createTopic("dev/topic"));
           // queueSender.setTimeToLive(2*1000);
            //queueSender.send(textMessage);
            mqProducer.send(textMessage);
            /*After sending a message we get message id */
            System.out.println("after sending a message we get message id "+ textMessage.getJMSMessageID());
            String jmsCorrelationID = " JMSCorrelationID = '" + textMessage.getJMSMessageID() + "'";


            /*Within the session we have to create queue reciver */
/*
            QueueReceiver queueReceiver = queueSession.createReceiver(queue,jmsCorrelationID);


            //Receive the message from
            Message message = queueReceiver.receive(60*1000);
            String responseMsg = ((TextMessage) message).getText();
*/
            mqProducer.close();
           // queueSender.close();
       //     queueReceiver.close();
            topicSession.close();
            topicConnection.close();


        } catch (JMSException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}