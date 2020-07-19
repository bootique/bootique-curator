/*
 * Licensed to ObjectStyle LLC under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ObjectStyle LLC licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.bootique.curator;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import io.bootique.BQRuntime;
import io.bootique.Bootique;
import io.bootique.junit5.BQApp;
import io.bootique.junit5.BQTest;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.function.Consumer;

import static org.junit.Assert.*;

@BQTest
@Testcontainers
public class CuratorFrameworkFactoryIT {

    private static final int HOST_PORT = 2181;
    private static final int CONTAINER_EXPOSED_PORT = 2181;
    private static final Consumer<CreateContainerCmd> MAPPING_CMD =
            e -> e.getHostConfig().withPortBindings(
                    new PortBinding(
                            Ports.Binding.bindPort(HOST_PORT),
                            new ExposedPort(CONTAINER_EXPOSED_PORT)
                    )
            );

    @Container
    public static GenericContainer zookeeper = new GenericContainer("zookeeper:3.6")
            .withCreateContainerCmdModifier(MAPPING_CMD)
            .withExposedPorts(CONTAINER_EXPOSED_PORT);

    @BQApp(skipRun = true)
    static final BQRuntime app = Bootique.app("--config=classpath:config.yml")
            .module(new CuratorModule())
            .createRuntime();

    @Test
    public void testFrameworkState() {
        CuratorFramework framework = app.getInstance(CuratorFramework.class);

        assertNotNull(framework);
        assertSame(framework.getState(), CuratorFrameworkState.STARTED);
    }

    @Test
    public void testSharedCounter() throws Exception {
        CuratorFramework framework = app.getInstance(CuratorFramework.class);

        // test is only about correct Curator integration, so we do nothing meaningful here
        try (SharedCount count = new SharedCount(framework, "/test_counter", 123)) {
            count.start();
            assertEquals(123, count.getCount());

            count.setCount(321);
            assertEquals(321, count.getCount());
        }
    }

    @Test
    public void testMutex() throws Exception {
        CuratorFramework framework = app.getInstance(CuratorFramework.class);

        // test is only about correct Curator integration, so we do nothing meaningful here
        InterProcessMutex mutex = new InterProcessMutex(framework, "/test_mutex");
        mutex.acquire();
        try {
            assertTrue(mutex.isAcquiredInThisProcess());
        } finally {
            mutex.release();
        }
    }
}
