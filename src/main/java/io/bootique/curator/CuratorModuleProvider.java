package io.bootique.curator;

import com.google.inject.Module;
import io.bootique.BQModuleProvider;

public class CuratorModuleProvider implements BQModuleProvider {

	@Override
	public Module module() {
		return new CuratorModule();
	}
}
