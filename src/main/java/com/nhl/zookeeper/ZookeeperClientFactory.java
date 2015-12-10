package com.nhl.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZookeeperClientFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperClientFactory.class);

	private String connectString;

	public CuratorFramework createZkClient() {

		LOGGER.info("Starting Zookeeper client, connecting to: " + connectString);

		RetryPolicy retryPolicy = new ExponentialBackoffRetry(20000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);

		// TODO: ensure CuratorFramework is closed on shutdown
		client.start();

		return client;
	}

	public String getConnectString() {
		return connectString;
	}

	public void setConnectString(String connectString) {
		this.connectString = connectString;
	}
}
