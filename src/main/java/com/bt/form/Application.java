package com.bt.form;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@SpringBootApplication
@Configuration
@ComponentScan({ "com.bt.form.controller", "com.bt.form.service", "com.bt.form.dao", "com.bt.form.exception",
		"com.bt.form.validator", "com.bt.form.util.EncryptionDemo", "com.bt.form.config" })
public class Application {

	private final Logger logger = LoggerFactory.getLogger(Application.class);
	private static final String KEY = "EncryptAnDecrypt";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource rb = new ResourceBundleMessageSource();
		rb.setBasenames("messages/messages", "messages/validation");
		return rb;
	}

	@Bean
	public Map<String, String> filesMap() {
		File folder = new File("src/main/resources/images");
		File[] listOfFiles = folder.listFiles();
		Map<String, String> fileMap = new HashMap<>();
		for (File file : listOfFiles) {
			if (file.isFile()) {
				String str = file.getName();
				fileMap.put(str.substring(0, str.indexOf("_")), str);
			}
		}
		for (Map.Entry<String, String> entry : fileMap.entrySet()) {
			logger.debug("Loaded user images with user id: {} ", entry.getKey());
			logger.debug("Loaded user images with user Image name : {} ", entry.getValue());
		}
		return fileMap;
	}

	@Bean
	public Cipher decipher() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		SecretKey key = new SecretKeySpec(KEY.getBytes(), "AES");
		Cipher decipher = Cipher.getInstance("AES");
		decipher.init(Cipher.DECRYPT_MODE, key);
		return decipher;
	}

	@Bean
	public SecretKey getSecretKey() {
		return new SecretKeySpec(KEY.getBytes(), "AES");
	}

}