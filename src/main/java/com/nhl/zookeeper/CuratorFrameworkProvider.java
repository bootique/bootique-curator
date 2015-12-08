package com.nhl.zookeeper;

import org.apache.curator.framework.CuratorFramework;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.nhl.launcher.config.ConfigSource;

public class CuratorFrameworkProvider implements Provider<CuratorFramework> {

	private ConfigSource configSource;

	@Inject
	public CuratorFrameworkProvider(ConfigSource configSource) {
		this.configSource = configSource;
	}

	@Override
	public CuratorFramework get() {
		return configSource.subconfig(ZookeeperModule.CONFIG_PREFIX, ZookeeperConfig.class).createZkClient();
	}
}
