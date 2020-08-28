package org.hillmerch.library.data.repositories;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.hillmerch.library.Application;
import org.hillmerch.library.web.BookController;
import org.hillmerch.library.exception.BookInfoNotFoundException;
import org.hillmerch.library.data.model.BookInfo;
import org.hillmerch.library.extension.DbCreationExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(DbCreationExtension.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("local")
@TestPropertySource(properties = { "amazon.dynamodb.endpoint=http://localhost:8000/", "amazon.aws.accesskey=test1", "amazon.aws.secretkey=test231" })
public class BookInfoRepositoryTest{

	private DynamoDBMapper dynamoDBMapper;

	@Autowired
	private AmazonDynamoDB amazonDynamoDB;

	@Autowired
	BookInfoRepository bookInfoRepository;

	@Autowired
	BookController bookController;

	private static final String EXPECTED_TITLE = "The Adventures of Tom Sawyer";

	@BeforeEach
	public void setup() {

		try {
			dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

			CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest( BookInfo.class);

			tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

			amazonDynamoDB.createTable(tableRequest);
		} catch (ResourceInUseException e) {
			// Do nothing, table already created
		}

		// TODO How to handle different environments. i.e. AVOID deleting all entries in BookInfo on table
		dynamoDBMapper.batchDelete(bookInfoRepository.findAll());
	}

	@Test
	public void givenItemWithExpectedTitle_whenRunFindAll_thenItemIsFound() {
		BookInfo productInfo = new BookInfo(EXPECTED_TITLE);
		bookInfoRepository.save(productInfo);

		List<BookInfo> result = (List<BookInfo>) bookInfoRepository.findAll();

		assertTrue( result.size() > 0 );
		assertEquals( EXPECTED_TITLE, result.get(0).getTitle());
	}

	@Test
	public void givenANonExistentId_whenRunGetBookInfo_thenThrowsBookInfoNotFoundException() {

		assertThrows( BookInfoNotFoundException.class, ()->{
			BookInfo result = bookController.getBookInfo( "1" );
		}  );

	}


}