package com.nhl.bootique.curator;

import com.google.inject.Module;
import com.nhl.bootique.BQModuleProvider;

public class CuratorModuleProvider implements BQModuleProvider {

	@Override
	public Module module() {
		return new CuratorModule();
	}
}
