package com.nhl.bootique.zookeeper;

import org.apache.curator.framework.CuratorFramework;

import com.google.inject.Provides;
import com.nhl.bootique.ConfigModule;
import com.nhl.bootique.config.ConfigurationFactory;

public class ZookeeperModule extends ConfigModule {

	public ZookeeperModule(String configPrefix) {
		super(configPrefix);
	}

	public ZookeeperModule() {
	}

	@Provides
	public CuratorFramework createCurator(ConfigurationFactory configFactory) {
		return configFactory.config(ZookeeperClientFactory.class, configPrefix).createZkClient();
	}
}
