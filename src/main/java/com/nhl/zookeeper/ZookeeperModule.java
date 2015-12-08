package com.nhl.zookeeper;

import org.apache.curator.framework.CuratorFramework;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.nhl.launcher.config.ConfigurationFactory;

public class ZookeeperModule implements Module {

	public static final String CONFIG_PREFIX = "zookeeper";

	@Override
	public void configure(Binder binder) {
		// do nothing.. configuration happens in @Provides
	}

	@Provides
	public CuratorFramework createCurator(ConfigurationFactory configSource) {
		return configSource.subconfig(ZookeeperModule.CONFIG_PREFIX, ZookeeperConfig.class).createZkClient();
	}
}
