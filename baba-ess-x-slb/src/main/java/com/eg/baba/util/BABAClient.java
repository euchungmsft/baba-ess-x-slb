package com.eg.baba.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;

public abstract class BABAClient {
	
	private String keyId;
	private String keySecret;
	private String endpoint;
	private DefaultProfile profile;
	public DefaultAcsClient client;

	public void init() {
		keyId = ClientProfile.getKeyId();
		keySecret = ClientProfile.getKeySecret();
		endpoint = ClientProfile.getEndpoint();
		
		profile = DefaultProfile.getProfile(endpoint, keyId, keySecret);
		client = new DefaultAcsClient(profile);				
	}
	
	abstract public boolean perform();
	
}
