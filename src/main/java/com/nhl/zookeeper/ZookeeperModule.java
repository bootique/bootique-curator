package com.nhl.zookeeper;

import org.apache.curator.framework.CuratorFramework;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;

public class ZookeeperModule implements Module {

	public static final String CONFIG_PREFIX = "zookeeper";

	@Override
	public void configure(Binder binder) {
		binder.bind(CuratorFramework.class).toProvider(CuratorFrameworkProvider.class).in(Singleton.class);
	}
}
