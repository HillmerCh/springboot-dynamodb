package org.hillmerch.library.extension;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;

public class DbCreationExtension implements AfterEachCallback, BeforeEachCallback {

	protected DynamoDBProxyServer server;

	public DbCreationExtension() {
		System.setProperty("sqlite4java.library.path", "native-libs");
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		String port = "8000";
		this.server = ServerRunner.createServerFromCommandLineArgs( new String[]{"-inMemory", "-port", port});
		server.start();
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception{
		this.stopUnchecked(server);
	}

	private void stopUnchecked(DynamoDBProxyServer dynamoDbServer) {
		try {
			dynamoDbServer.stop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
