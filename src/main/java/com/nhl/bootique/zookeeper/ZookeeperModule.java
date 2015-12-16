package com.nhl.bootique.zookeeper;

import org.apache.curator.framework.CuratorFramework;

import com.google.inject.Provides;
import com.nhl.bootique.FactoryModule;
import com.nhl.bootique.factory.FactoryConfigurationService;

public class ZookeeperModule extends FactoryModule<ZookeeperClientFactory> {

	public ZookeeperModule(String configPrefix) {
		super(ZookeeperClientFactory.class, configPrefix);
	}

	public ZookeeperModule() {
		super(ZookeeperClientFactory.class);
	}

	@Provides
	public CuratorFramework createCurator(FactoryConfigurationService configService) {
		return createFactory(configService).createZkClient();
	}

}
