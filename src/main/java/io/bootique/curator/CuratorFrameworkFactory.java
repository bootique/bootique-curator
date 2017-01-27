package io.bootique.curator;

import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@BQConfig
public class CuratorFrameworkFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(CuratorFrameworkFactory.class);

    private String connectString;

    public CuratorFramework createZkClient() {

        LOGGER.info("Starting CuratorFramework, connecting to Zookeeper: " + connectString);

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(20000, 3);
        return org.apache.curator.framework.CuratorFrameworkFactory.newClient(connectString, retryPolicy);
    }

    @BQConfigProperty("Zookeeper connection String. E.g. 127.0.0.1:2181,127.0.0.1:2182")
    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }
}
