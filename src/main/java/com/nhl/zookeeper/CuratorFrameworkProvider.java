package com.nhl.zookeeper;

import org.apache.cayenne.di.DIRuntimeException;
import org.apache.cayenne.di.Inject;
import org.apache.cayenne.di.Provider;
import org.apache.curator.framework.CuratorFramework;

import com.nhl.launcher.config.ConfigSource;

public class CuratorFrameworkProvider implements Provider<CuratorFramework> {

	private ConfigSource configSource;

	public CuratorFrameworkProvider(@Inject ConfigSource configSource) {
		this.configSource = configSource;
	}

	@Override
	public CuratorFramework get() throws DIRuntimeException {
		return configSource.subconfig(ZookeeperModule.CONFIG_PREFIX, ZookeeperConfig.class).createZkClient();
	}
}
