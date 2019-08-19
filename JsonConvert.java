package jsonMain;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonConvert {

	
	
	public String go () {
		
		  StringWriter data=new StringWriter(); 
		  
	       ObjectMapper mapper = new ObjectMapper();
	       HashMap h=new HashMap();
	        Vector v = createStaff();

	        try {

	        	  for (int index = 0; index < v.size(); index++) {            
	                  Object o=v.get(index);       
	                  System.out.println("oo=" + o.getClass().getSimpleName());
	                  
	        	 
	        	
	            // Java objects to JSON file
	          //  mapper.writeValue(data, staff);
	        		            // Java objects to JSON string - compact-print
   	               //String jsonString = mapper.writeValueAsString(o);
 	                 h.put(o.getClass().getSimpleName(), o);
 	            }

	        	  
	        //	  System.out.println(jsonString);

	            // Java objects to JSON string - pretty-print
	            String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(h);

	            System.out.println(jsonInString2);
	            return jsonInString2;     
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	       return null;
	    }

	

private static Vector createStaff() {

	Vector v=new Vector();
	
    Staff staff = new Staff();

    staff.setName("mkyong");
    staff.setAge(38);
    staff.setPosition(new String[]{"Founder", "CTO", "Writer"});
    
    Calendar myCalendar = new GregorianCalendar(2014, 2, 11);
    Date myDate = myCalendar.getTime();
    
    
    staff.setDt(myDate);
    HashMap salary = new HashMap() {{
        put("2010", new BigDecimal(10000));
        put("2012", new BigDecimal(12000));
        put("2018", new BigDecimal(14000));
    }};
    staff.setSalary(salary);
    staff.setSkills(Arrays.asList("java", "python", "node", "kotlin"));

    Place p=new Place();
    p.setAaa("hello world");
    p.setB(34343);
    
    
    v.add(staff);
    v.add(p);
    return v;

}




	public  void sendRequestMsg(String messageId, String payload) throws JMSException {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
		env.put(Context.PROVIDER_URL, "file:///home/myapp/mq_admin_objects"); //mq_admin_objects is a folder containing the .bindings file
		                                                                      //.bindings file contains host name, channel, queue names, etc defined
		Context  ctx = null;
		javax.jms.QueueConnectionFactory  cf = null;
		
		QueueConnection reqConnection = null;

		try {
			ctx = new InitialContext(env);
			cf = (javax.jms.QueueConnectionFactory) ctx.lookup("JMS_QCF_MYAPP");
			reqConnection = cf.createQueueConnection();
			QueueSession qSession = reqConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			
			Queue reqQueue = (Queue)ctx.lookup("MYAPP_REQUEST.JREQUEST");
			Queue respQueue = (Queue)ctx.lookup("MYAPP_RESPONSE.JREQUEST");
			QueueSender qSender = qSession.createSender(reqQueue);
			TextMessage reqMsg = qSession.createTextMessage();
			reqMsg.setText(payload);

			reqMsg.setJMSMessageID(messageId);
			reqMsg.setJMSCorrelationID(messageId);
			reqMsg.setJMSExpiration(28800000);
		//	reqMsg.setIntProperty(JmsConstants.JMS_IBM_CHARACTER_SET, 1208);
			//reqMsg.setStringProperty(JmsConstants.JMS_IBM_FORMAT, CMQC.MQFMT_STRING);
			reqMsg.setJMSReplyTo(respQueue);
			qSender.send(reqMsg);
			
		} catch (NamingException e) {
			e.printStackTrace();
		} finally {
			reqConnection.close();
		}
	}
	


}
