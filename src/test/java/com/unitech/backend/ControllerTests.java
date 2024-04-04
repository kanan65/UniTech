package com.unitech.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unitech.backend.account.model.Account;
import com.unitech.backend.account.model.AccountDTO;
import com.unitech.backend.apiResponse.ApiResponseDTO;
import com.unitech.backend.apiResponse.LoginResponseDTO;
import com.unitech.backend.enums.ResponseEnum;
import com.unitech.backend.request.CurrencyRequestDTO;
import com.unitech.backend.request.LoginRequestDTO;
import com.unitech.backend.request.RegisterRequestDTO;
import com.unitech.backend.request.TransferRequestDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.remote.client.HttpHeaderInterceptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ControllerTests {

	@Autowired
	TestRestTemplate restTemplate;

	public static String randomPinString;
	public static String authToken;
	public static String firstIdentifier;
	public static String secIdentifier;

	@Test
	@Order(1)
	void registerTest() {
		Random rand = new Random();
		int randomPin = rand.nextInt(9999999);
		ControllerTests.randomPinString = String.valueOf(randomPin);

		RegisterRequestDTO request = new RegisterRequestDTO();
		request.setPin(ControllerTests.randomPinString);
		request.setName("TestName");
		request.setPassword("TestPassW");

		ResponseEntity<ApiResponseDTO> responseEntity = restTemplate
				.postForEntity("/public-api/user/register", request, ApiResponseDTO.class);

		assertEquals(OK, responseEntity.getStatusCode());
	}

	@Test
	@Order(2)
	void registerAgainTest() {

		RegisterRequestDTO request = new RegisterRequestDTO();
		request.setPin(ControllerTests.randomPinString);
		request.setName("TestName");
		request.setPassword("TestPassW");

		ResponseEntity<ApiResponseDTO> responseEntity = restTemplate
				.postForEntity("/public-api/user/register", request, ApiResponseDTO.class);

		assertEquals(OK, responseEntity.getStatusCode());
		assertEquals(ResponseEnum.SAME_PIN_ERROR_MESSAGE.getMessage(), responseEntity.getBody().getMessage());
	}

	@Test
	@Order(3)
	void login() {

		LoginRequestDTO requestDTO = new LoginRequestDTO();
		requestDTO.setPin(ControllerTests.randomPinString);
		requestDTO.setPassword("TestPassW");

		ResponseEntity<ApiResponseDTO> responseEntity = restTemplate
				.postForEntity("/public-api/user/login", requestDTO, ApiResponseDTO.class);

		assertEquals(OK, responseEntity.getStatusCode());

		ObjectMapper objectMapper = new ObjectMapper();
		LoginResponseDTO loginResponse = objectMapper.convertValue(responseEntity.getBody().getData(), LoginResponseDTO.class);
		ControllerTests.authToken = "Bearer " + loginResponse.getToken();
	}

	@Test
	@Order(4)
	void loginBadWithCredentials() {

		LoginRequestDTO requestDTO = new LoginRequestDTO();
		requestDTO.setPin("random_credential");
		requestDTO.setPassword("TestPassW");

		ResponseEntity<ApiResponseDTO> responseEntity = restTemplate
				.postForEntity("/public-api/users/login", requestDTO, ApiResponseDTO.class);

		assertEquals(OK, responseEntity.getStatusCode());
		assertEquals(ResponseEnum.BAD_CREDENTIALS_ERROR_MESSAGE.getMessage(), responseEntity.getBody().getMessage());
	}


	@Test
	@Order(5)
	void addAccountTest() {
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new HttpHeaderInterceptor("Authorization", ControllerTests.authToken));
		restTemplate.getRestTemplate().setInterceptors(interceptors);

		ResponseEntity<ApiResponseDTO> responseEntity = restTemplate
				.postForEntity("/public-api/account/addAccount", null, ApiResponseDTO.class);

		assertEquals(OK, responseEntity.getStatusCode());
		assertEquals(ResponseEnum.SUCCESS_MESSAGE.getMessage(), responseEntity.getBody().getMessage());

		ObjectMapper objectMapper = new ObjectMapper();
		Account account = objectMapper.convertValue(responseEntity.getBody().getData(), Account.class);
		ControllerTests.firstIdentifier = account.getIdentifier();
	}

	@Test
	@Order(5)
	void addSecondAccountTest() {
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new HttpHeaderInterceptor("Authorization", ControllerTests.authToken));
		restTemplate.getRestTemplate().setInterceptors(interceptors);

		ResponseEntity<ApiResponseDTO> responseEntity = restTemplate
				.postForEntity("/public-api/account", null, ApiResponseDTO.class);

		assertEquals(OK, responseEntity.getStatusCode());
		assertEquals(ResponseEnum.SUCCESS_MESSAGE.getMessage(), responseEntity.getBody().getMessage());

		ObjectMapper objectMapper = new ObjectMapper();
		Account account = objectMapper.convertValue(responseEntity.getBody().getData(), Account.class);
		ControllerTests.secIdentifier = account.getIdentifier();
	}

	@Test
	@Order(6)
	void addBalanceToFirstTest() {

		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setIdentifier(ControllerTests.firstIdentifier);
		accountDTO.setAccountBalance(BigDecimal.valueOf(100));

		ResponseEntity<ApiResponseDTO> responseEntity = restTemplate
				.postForEntity("/public-api/account/addBalance", accountDTO, ApiResponseDTO.class);

		assertEquals(OK, responseEntity.getStatusCode());
		assertEquals(ResponseEnum.SUCCESS_MESSAGE.getMessage(), responseEntity.getBody().getMessage());
	}


	@Test
	@Order(7)
	void sendAmountToTheSameAccountTest() {

		TransferRequestDTO requestDTO = new TransferRequestDTO();
		requestDTO.setFromIdentifier(ControllerTests.firstIdentifier);
		requestDTO.setToIdentifier(ControllerTests.firstIdentifier);
		requestDTO.setAmount(BigDecimal.valueOf(10));

		ResponseEntity<ApiResponseDTO> responseEntity = restTemplate
				.postForEntity("/public-api/transfer/makeTransfer", requestDTO, ApiResponseDTO.class);

		assertEquals(OK, responseEntity.getStatusCode());
		assertEquals(ResponseEnum.SAME_ACCOUNT_ERROR_MESSAGE.getMessage(), responseEntity.getBody().getMessage());
	}

	@Test
	@Order(8)
	void sendAmountToOtherAccountTest() {

		TransferRequestDTO requestDTO = new TransferRequestDTO();
		requestDTO.setFromIdentifier(ControllerTests.firstIdentifier);
		requestDTO.setToIdentifier(ControllerTests.secIdentifier);
		requestDTO.setAmount(BigDecimal.valueOf(10));

		ResponseEntity<ApiResponseDTO> responseEntity = restTemplate
				.postForEntity("/public-api/transfer/makeTransfer", requestDTO, ApiResponseDTO.class);

		assertEquals(OK, responseEntity.getStatusCode());
		assertEquals(ResponseEnum.SUCCESS_MESSAGE.getMessage(), responseEntity.getBody().getMessage());
	}

	@Test
	@Order(9)
	void insufficientBalanceTest() {

		TransferRequestDTO requestDTO = new TransferRequestDTO();
		requestDTO.setFromIdentifier(ControllerTests.firstIdentifier);
		requestDTO.setToIdentifier(ControllerTests.secIdentifier);
		requestDTO.setAmount(BigDecimal.valueOf(1000));

		ResponseEntity<ApiResponseDTO> responseEntity = restTemplate
				.postForEntity("/public-api/transfer/makeTransfer", requestDTO, ApiResponseDTO.class);

		assertEquals(OK, responseEntity.getStatusCode());
		assertEquals(ResponseEnum.INSUFFICIENT_BALANCE_ERROR_MESSAGE.getMessage(), responseEntity.getBody().getMessage());
	}
}