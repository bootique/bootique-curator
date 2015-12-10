package com.nhl.zookeeper;

import org.apache.curator.framework.CuratorFramework;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.nhl.launcher.config.FactoryConfigurationService;

public class ZookeeperBundle {

	private static final String CONFIG_PREFIX = "zookeeper";

	private String configPrefix;

	public static Module zkModule() {
		return zkModule(CONFIG_PREFIX);
	}

	public static Module zkModule(String configPrefix) {
		return new ZookeeperBundle(configPrefix).module();
	}

	private ZookeeperBundle(String configPrefix) {
		this.configPrefix = configPrefix;
	}

	public Module module() {
		return new ZookeeperModule();
	}

	class ZookeeperModule implements Module {

		@Override
		public void configure(Binder binder) {
			// do nothing.. configuration happens in @Provides
		}

		@Provides
		public CuratorFramework createCurator(FactoryConfigurationService configService) {
			return configService.factory(ZookeeperClientFactory.class, configPrefix).createZkClient();
		}
	}
}
