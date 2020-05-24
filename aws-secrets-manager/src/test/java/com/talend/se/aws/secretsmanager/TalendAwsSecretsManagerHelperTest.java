package com.talend.se.aws.secretsmanager;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TalendAwsSecretsManagerHelperTest {

	private static final String REGION = "us-east-1";
	private TalendAwsSecretsManagerHelper helper;
	private static final Logger logger = LoggerFactory.getLogger(TalendAwsSecretsManagerHelperTest.class);
	
	@Before
	public void setUp() throws Exception {
		logger.info("TalendAwsSecretsManagerHelperTest.setup");
		helper = new TalendAwsSecretsManagerHelper(REGION);
	}

	@Test
	public void testGet() {
		logger.info("TalendAwsSecretsManagerHelperTest.testGet");
		final String EXPECTED = "not_my_real_password";
		
		String secretName = "not_my_real_password";
		String secret = helper.get(secretName);
		logger.info("secret={}", secret);
		assertEquals(EXPECTED, secret);
	}

}
