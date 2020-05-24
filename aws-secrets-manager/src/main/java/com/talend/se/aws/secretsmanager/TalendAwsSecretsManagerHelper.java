package com.talend.se.aws.secretsmanager;

import com.amazonaws.services.secretsmanager.*;
import com.amazonaws.services.secretsmanager.model.*;
//import com.amazonaws.util.*;
//import com.amazonaws.util.Base64;

public class TalendAwsSecretsManagerHelper {

	private static final String DEFAULT_REGION = "us-east-1";
    private String region;
    private AWSSecretsManager client;
    
	public TalendAwsSecretsManagerHelper(String region) {
		this.region = region;
		
		client = AWSSecretsManagerClientBuilder.standard().withRegion(this.region).build();
	}
	
	public TalendAwsSecretsManagerHelper() {
		this(DEFAULT_REGION);
	}
	
	public String get(String secretName) {
	    String secret;
	    
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(secretName);
        GetSecretValueResult getSecretValueResult = client.getSecretValue(getSecretValueRequest);
        secret = getSecretValueResult.getSecretString();
		return secret;
	}
	
}
