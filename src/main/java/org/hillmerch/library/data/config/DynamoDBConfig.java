package org.hillmerch.library.data.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

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

	@Bean
	public AmazonDynamoDB amazonDynamoDB() {
		AmazonDynamoDB amazonDynamoDB
				= AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration (amazonDynamoDBEndpoint, amazonAWSRegion) )
				.withCredentials(new AWSStaticCredentialsProvider( new BasicAWSCredentials( amazonAWSAccessKey, amazonAWSSecretKey)))
				.build();
		return amazonDynamoDB;
	}

}
