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

import io.bootique.BQCoreModule;
import io.bootique.BQRuntime;
import io.bootique.junit5.BQTest;
import io.bootique.junit5.BQTestFactory;
import io.bootique.junit5.BQTestTool;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;

import static org.junit.Assert.*;


@BQTest
public abstract class CuratorFrameworkBaseTest {

    protected static GenericContainer createZookeeper(String container) {
        return new GenericContainer(container).withExposedPorts(2181);
    }

    @BQTestTool
    private BQTestFactory testFactory = new BQTestFactory();

    protected BQRuntime createRuntime() {
        int port = getZk().getMappedPort(2181);
        return testFactory
                .app()
                .autoLoadModules()
                .module(b -> BQCoreModule.extend(b).setProperty("bq.curator.connectString", "localhost:" + port))
                .createRuntime();
    }

    protected abstract GenericContainer getZk();

    @Test
    public void testCurator() throws Exception {
        CuratorFramework framework = createRuntime().getInstance(CuratorFramework.class);

        testFrameworkState(framework);
        testSharedCounter(framework);
        testMutex(framework);
    }


    private void testFrameworkState(CuratorFramework framework) {

        assertNotNull(framework);
        assertSame(framework.getState(), CuratorFrameworkState.STARTED);
    }


    private void testSharedCounter(CuratorFramework framework) throws Exception {

        // test is only about correct Curator integration, so we do nothing meaningful here
        try (SharedCount count = new SharedCount(framework, "/test_counter", 123)) {
            count.start();
            assertEquals(123, count.getCount());

            count.setCount(321);
            assertEquals(321, count.getCount());
        }
    }
    
    private void testMutex(CuratorFramework framework) throws Exception {

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
