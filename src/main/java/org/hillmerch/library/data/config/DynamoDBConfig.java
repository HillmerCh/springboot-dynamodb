package org.hillmerch.library.data.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

@Configuration
@EnableDynamoDBRepositories(basePackages = "org.hillmerch.library.data.repositories")
public class DynamoDBConfig {


	@Value("${amazon.dynamodb.endpoint}")
	private String amazonDynamoDBEndpoint;

	@Value("${amazon.aws.accesskey}")
	private String amazonAWSAccessKey;

	@Value("${amazon.aws.secretkey}")
	private String amazonAWSSecretKey;

	@Value("${amazon.aws.region}")
	private String amazonAWSRegion;

	@Autowired
	private ApplicationContext context;

	@Bean
	public AmazonDynamoDB amazonDynamoDB() {

		AmazonDynamoDB amazonDynamoDB
				= AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration (amazonDynamoDBEndpoint, amazonAWSRegion) )
				.withCredentials(new AWSStaticCredentialsProvider( new BasicAWSCredentials( amazonAWSAccessKey, amazonAWSSecretKey)))
				.build();
		return amazonDynamoDB;
	}


	@Bean
	public AWSCredentials amazonAWSCredentials() {
		return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
	}

	@Bean(name = "mvcHandlerMappingIntrospectorCustom")
	public HandlerMappingIntrospector mvcHandlerMappingIntrospectorCustom() {
		return new HandlerMappingIntrospector(context);
	}
}
