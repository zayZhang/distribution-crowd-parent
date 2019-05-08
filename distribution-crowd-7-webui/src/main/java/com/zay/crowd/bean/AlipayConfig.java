package com.zay.crowd.bean;

import java.io.FileWriter;
import java.io.IOException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlipayConfig implements InitializingBean {
	
	@Value("${alipay.app_id}")
	private String appId;

	@Value("${alipay.merchant_private_key}")
	private String merchantPrivateKey;

	@Value("${alipay.alipay_public_key}")
	private String alipayPublicKey;

	@Value("${alipay.notify_url}")
	private String notifyUrl;

	@Value("${alipay.return_url}")
	private String returnUrl;

	@Value("${alipay.sign_type}")
	private String signType;

	@Value("${alipay.charset}")
	private String charsetValue;

	@Value("${alipay.gatewayUrl}")
	private String gatewayUrlValue;
	
	public static String app_id;
	
	public static String merchant_private_key;

	public static String alipay_public_key;
	
	public static String notify_url;
	
	public static String return_url;
	
	public static String sign_type;
	
	public static String charset;
	
	public static String gatewayUrl;
										
	public static String logPath = "d:\\";

    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(logPath + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO 
		app_id = this.appId;
		
		merchant_private_key = this.merchantPrivateKey;
		
		alipay_public_key = this.alipayPublicKey;
		
		notify_url = this.notifyUrl;
		
		return_url = this.returnUrl;
		
		sign_type = this.signType;
		
		charset = this.charsetValue;
		
		gatewayUrl = this.gatewayUrlValue;
	}
}

