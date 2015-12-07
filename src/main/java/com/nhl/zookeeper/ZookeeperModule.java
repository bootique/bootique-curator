package com.nhl.zookeeper;

import org.apache.cayenne.di.Binder;
import org.apache.cayenne.di.Module;
import org.apache.curator.framework.CuratorFramework;

public class ZookeeperModule implements Module {

	public static final String CONFIG_PREFIX = "zookeeper";

	@Override
	public void configure(Binder binder) {
		binder.bind(CuratorFramework.class).toProvider(CuratorFrameworkProvider.class);
	}
}
