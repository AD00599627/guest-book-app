
package com.bt.form.web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URL;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bt.form.model.LoginForm;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
	private final static String url = "http://localhost:";
	private final static String root = "/guest-book-app/";

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	SecretKey key;
	
	@Autowired
	public void setSecretKey(SecretKey key) {
		this.key = key;
	}

	@Test
	void UserLogin() throws Exception {
		ResponseEntity<String> response = restTemplate.getForEntity(new URL(url + port + root).toString(),
				String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());

	}

	@Test
	void UserLoginNoData() throws Exception {
		LoginForm loginForm = new LoginForm();
		ResponseEntity<String> response = restTemplate
				.postForEntity(new URL(url + port + root + "/users/login").toString(), loginForm, String.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

	}

	@Test
	void userLoginWithData() throws Exception {
		LoginForm loginForm = new LoginForm();
		loginForm.setEmail("abc12@gmail.com");
		loginForm.setPassword("11");
		ResponseEntity<String> response = restTemplate
				.postForEntity(new URL(url + port + root + "/users/login").toString(), loginForm, String.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	void userdelete() throws Exception {
		LoginForm loginForm = new LoginForm();
		loginForm.setEmail("abc12@gmail.com");
		loginForm.setPassword("11");
		ResponseEntity<String> response = restTemplate
				.postForEntity(new URL(url + port + root + "/users/4/delete").toString(), 4, String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void userapprove() throws Exception {
		LoginForm loginForm = new LoginForm();
		loginForm.setEmail("abc12@gmail.com");
		loginForm.setPassword("11");
		ResponseEntity<String> response = restTemplate
				.getForEntity(new URL(url + port + root + "/users/1/approve").toString(), String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
}
