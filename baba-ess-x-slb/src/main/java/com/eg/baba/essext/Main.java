package com.eg.baba.essext;

import java.util.Iterator;
import java.util.List;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQObjectMessage;

import com.aliyuncs.ecs.model.v20140526.StopInstanceRequest;
import com.aliyuncs.ecs.model.v20140526.StopInstanceResponse;
import com.aliyuncs.ess.model.v20140828.ExecuteScalingRuleRequest;
import com.aliyuncs.ess.model.v20140828.ExecuteScalingRuleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.slb.model.v20140515.DescribeHealthStatusRequest;
import com.aliyuncs.slb.model.v20140515.DescribeHealthStatusResponse;
import com.aliyuncs.slb.model.v20140515.DescribeHealthStatusResponse.BackendServer;
import com.eg.baba.cms.alert.CMSAlert;
import com.eg.baba.util.BABAClient;
import com.eg.baba.util.ClientProfile;

public class Main 
	extends BABAClient{

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Main main = new Main();
		main.init();
		main.perform();
		
	}

	@Override
	public boolean perform() {
		// TODO Auto-generated method stub
		
		boolean stopInstanceOnFailure = ClientProfile.getConfig("stop-on-failure").equals("true");
		
		String regionId = ClientProfile.getConfig("region-id");
		String slbId = ClientProfile.getConfig("slb-id");
		String actionAri = ClientProfile.getConfig("acrion-ari");
		String webhookUrl = ClientProfile.getConfig("webhook-url");
		
		System.out.println("Stop instance on Failure : "+stopInstanceOnFailure);
		System.out.println("Region ID : "+regionId);
		System.out.println("SLB ID : "+slbId);
		System.out.println("Action ARI : "+actionAri);
		System.out.println("Webhook URL : "+webhookUrl);
		
		if(webhookUrl != null && webhookUrl.trim().length() > 0) {
			try {
				
				runAlertListener();
				
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			
			boolean succ = checkSLB(regionId, slbId, stopInstanceOnFailure);
			
			if(!succ && !stopInstanceOnFailure) {	//	fail on any abnormal
				runTheAction(regionId, actionAri);
			}
			
		}
		
		return true;
	}

	private void runAlertListener() throws JMSException {
		// TODO Auto-generated method stub
		
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		factory.setTrustAllPackages(true);
	    Connection connection = factory.createConnection();
	    connection.start();
	    connection.setExceptionListener(new ExceptionListener(){

			@Override
			public void onException(JMSException ex) {
				// TODO Auto-generated method stub
				ex.printStackTrace();
			}});
	    
	    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	    Destination dest = session.createQueue("alert-queue");
	    MessageConsumer consumer = session.createConsumer(dest);
	    
	    consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				// TODO Auto-generated method stub
				
				try {
					
					ActiveMQObjectMessage msg = (ActiveMQObjectMessage) message;
					CMSAlert alert = (CMSAlert) msg.getObject();
					System.out.println(alert.getMsg().toString());
					
					//	do something 
					

					
					
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}});
	    
	    System.out.println("Session Done");

	}

	private boolean runTheAction(String regionId, String actionAri) {
		// TODO Auto-generated method stub
		
		ExecuteScalingRuleRequest req = new ExecuteScalingRuleRequest();
		req.setRegionId(regionId);
		req.setScalingRuleAri(actionAri);
		
		try {

			ExecuteScalingRuleResponse res = client.getAcsResponse(req);
			ClientProfile.printObject(res);
			
			return true;

		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return false;			
		
	}

	private boolean checkSLB(String regionId, String slbId, boolean stopOnAbnormal) {
		// TODO Auto-generated method stub
		
		DescribeHealthStatusRequest req = new DescribeHealthStatusRequest();
		req.setRegionId(regionId);
		req.setLoadBalancerId(slbId);
		
		try {

			DescribeHealthStatusResponse res = client.getAcsResponse(req);
			ClientProfile.printObject(res);
			
			int failCount = 0;
			List<BackendServer> lst = res.getBackendServers();
			for(Iterator<BackendServer> i = lst.iterator(); i.hasNext();) {
				BackendServer server = i.next();
				String serverId = server.getServerId();
				String serverStatus = server.getServerHealthStatus();
				
				if(serverStatus != null && !serverStatus.trim().equalsIgnoreCase("normal")) {
					//	do soemthing for abnormal
					
					failCount++;
					if(stopOnAbnormal)
						stopInstance(regionId, serverId);
				}
			}
			
			if(failCount >= 1)
				return false;
			
			return true;

		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return false;

	}

	private boolean stopInstance(String regionId, String instanceId) {
		// TODO Auto-generated method stub
		StopInstanceRequest req = new StopInstanceRequest();
		req.setRegionId(regionId);
		req.setInstanceId(instanceId);

		try {

			StopInstanceResponse res = client.getAcsResponse(req);
			ClientProfile.printObject(res);

			return true;

		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return false;
	}

}
