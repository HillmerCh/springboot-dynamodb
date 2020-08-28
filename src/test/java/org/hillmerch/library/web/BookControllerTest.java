package org.hillmerch.library.web;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;

import org.hillmerch.library.data.model.BookInfo;
import org.hillmerch.library.extension.DbCreationExtension;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(DbCreationExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
@TestPropertySource(properties = { "amazon.dynamodb.endpoint=http://localhost:8000/", "amazon.aws.accesskey=test1", "amazon.aws.secretkey=test231" })
public class BookControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private String restUrl;

	private static final String EXPECTED_TITLE = "The Adventures of Tom Sawyer";

	private BookInfo createBookInfo(String title) throws JSONException {
		JSONObject bookInfoJsonObject = new JSONObject();
		bookInfoJsonObject.put("title", title);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>(bookInfoJsonObject.toString(), headers);

		return this.restTemplate.postForObject( restUrl, request, BookInfo.class );

		//return restTemplate.postForObject(restUrl, new BookInfo(EXPECTED_TITLE), BookInfo.class); With a Object instead of JSON
	}

	@Test
	public void newBookInfo() throws JSONException {
		BookInfo bookInfo =  this.createBookInfo( EXPECTED_TITLE );
		assertNotNull( bookInfo );
	}

	@Test
	public void givenANonExistentId_whenRunGetBookInfo_thenThrowsBookInfoNotFoundException() {
		ResponseEntity<String> response = restTemplate.getForEntity(restUrl + "/1", String.class);
		assertEquals( HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void allBookInfos() {
		try {
			BookInfo bookInfo =  this.createBookInfo( EXPECTED_TITLE );
		}catch (JSONException e) {
			e.printStackTrace();
		}

		ResponseEntity<BookInfo[]> response = restTemplate.getForEntity(restUrl , BookInfo[].class);
		List<BookInfo> bookInfoList = Arrays.asList(response.getBody());

		assertTrue( bookInfoList.size() > 0 );
		assertEquals( EXPECTED_TITLE, bookInfoList.get(0) .getTitle());
	}


	private DynamoDBMapper dynamoDBMapper;

	@Autowired
	private AmazonDynamoDB amazonDynamoDB;

	@BeforeEach
	public void setup()  {

		restUrl = "http://localhost:" + port +"/api/books";

		try {
			dynamoDBMapper = new DynamoDBMapper( amazonDynamoDB);

			CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest( BookInfo.class);

			tableRequest.setProvisionedThroughput(new ProvisionedThroughput( 1L, 1L));

			amazonDynamoDB.createTable(tableRequest);
		} catch (ResourceInUseException e) {
			// Do nothing, table already created
		}
	}


}