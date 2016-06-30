package com.nhl.bootique.curator;

import org.apache.curator.framework.CuratorFramework;

import com.google.inject.Provides;
import com.nhl.bootique.ConfigModule;
import com.nhl.bootique.config.ConfigurationFactory;
import com.nhl.bootique.log.BootLogger;
import com.nhl.bootique.shutdown.ShutdownManager;

public class CuratorModule extends ConfigModule {

	public CuratorModule(String configPrefix) {
		super(configPrefix);
	}

	public CuratorModule() {
	}

	@Provides
	public CuratorFramework createCurator(ConfigurationFactory configFactory, BootLogger bootLogger,
			ShutdownManager shutdownManager) {
		CuratorFramework client = configFactory.config(CuratorFrameworkFactory.class, configPrefix).createZkClient();

		shutdownManager.addShutdownHook(() -> {
			bootLogger.trace(() -> "shutting down Curator...");
			client.close();
		});

		return client;
	}
}
