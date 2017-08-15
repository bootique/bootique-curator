package io.bootique.curator;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.bootique.ConfigModule;
import io.bootique.config.ConfigurationFactory;
import io.bootique.log.BootLogger;
import io.bootique.shutdown.ShutdownManager;
import org.apache.curator.framework.CuratorFramework;

public class CuratorModule extends ConfigModule {

	public CuratorModule(String configPrefix) {
		super(configPrefix);
	}

	public CuratorModule() {
	}

	@Provides
	@Singleton
	public CuratorFramework createCurator(ConfigurationFactory configFactory, BootLogger bootLogger,
										  ShutdownManager shutdownManager) {
		CuratorFramework client = configFactory.config(CuratorFrameworkFactory.class, configPrefix).createZkClient();

		shutdownManager.addShutdownHook(() -> {
			bootLogger.trace(() -> "shutting down Curator...");
			client.close();
		});

		client.start();

		return client;
	}
}
