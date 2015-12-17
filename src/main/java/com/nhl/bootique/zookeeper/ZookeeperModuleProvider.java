package com.nhl.bootique.zookeeper;

import com.google.inject.Module;
import com.nhl.bootique.BQModuleProvider;

public class ZookeeperModuleProvider implements BQModuleProvider {

	@Override
	public Module module() {
		return new ZookeeperModule();
	}
}
