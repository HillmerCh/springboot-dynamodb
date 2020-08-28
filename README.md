# Library SpringBoot-DynamoDb

This repository uses Java, SpringBoot with DynamoDB 


* Run `mvn verify`

* Test can be run with a local in-memory DynamoDB

* To use a DynamoDb is necessary to update the aws properties in the file
src/main/resources/applications.properties 


        amazon.dynamodb.endpoint=http://localhost:8000/
        amazon.aws.accesskey=<KEY>
        amazon.aws.secretkey=<SECRET_KEY>
        amazon.aws.region=<REGION>
    
    


