package io.bootique.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CuratorFrameworkFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(CuratorFrameworkFactory.class);

    private String connectString;

    public CuratorFramework createZkClient() {

        LOGGER.info("Starting CuratorFranework, connecting to Zookeeper: " + connectString);

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(20000, 3);
        CuratorFramework client = org.apache.curator.framework.CuratorFrameworkFactory.newClient(connectString, retryPolicy);

        // TODO: ensure CuratorFramework is closed on shutdown
        client.start();

        return client;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }
}
