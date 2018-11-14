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

import java.util.function.Consumer;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import io.bootique.BQRuntime;
import io.bootique.test.junit.BQTestFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CuratorFrameworkFactoryIT {

    private static final int HOST_PORT = 2181;
    private static final int CONTAINER_EXPOSED_PORT = 2181;
    private static final Consumer<CreateContainerCmd> MAPPING_CMD =
            e -> e.withPortBindings(
                    new PortBinding(
                            Ports.Binding.bindPort(HOST_PORT),
                            new ExposedPort(CONTAINER_EXPOSED_PORT)
                    )
            );

    @ClassRule
    public static GenericContainer zookeeper = new GenericContainer("zookeeper:latest")
            .withCreateContainerCmdModifier(MAPPING_CMD)
            .withExposedPorts(CONTAINER_EXPOSED_PORT);

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    @Test
    public void testFrameworkState() {
        CuratorFramework framework = crateCuratorFramework();

        assertNotNull(framework);
        assertSame(framework.getState(), CuratorFrameworkState.STARTED);
        // check that correct version of Zookeeper is used
        assertTrue(framework.isZk34CompatibilityMode());
    }

    @Test
    public void testSharedCounter() throws Exception {
        CuratorFramework framework = crateCuratorFramework();

        // test is only about correct Curator integration, so we do nothing meaningful here
        try(SharedCount count = new SharedCount(framework, "/test_counter", 123)) {
            count.start();
            assertEquals(123, count.getCount());

            count.setCount(321);
            assertEquals(321, count.getCount());
        }
    }

    @Test
    public void testMutex() throws Exception {
        CuratorFramework framework = crateCuratorFramework();

        // test is only about correct Curator integration, so we do nothing meaningful here
        InterProcessMutex mutex = new InterProcessMutex(framework, "/test_mutex");
        mutex.acquire();
        try {
            assertTrue(mutex.isAcquiredInThisProcess());
        } finally {
            mutex.release();
        }
    }

    private CuratorFramework crateCuratorFramework() {
        BQRuntime bqRuntime = testFactory
                .app("--config=classpath:config.yml")
                .module(new CuratorModule())
                .createRuntime();
        return bqRuntime.getInstance(CuratorFramework.class);
    }
}
