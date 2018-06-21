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

import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@BQConfig
public class CuratorFrameworkFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(CuratorFrameworkFactory.class);

    private String connectString;

    public CuratorFramework createZkClient() {

        LOGGER.info("Starting CuratorFramework, connecting to Zookeeper: " + connectString);

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(20000, 3);
        return org.apache.curator.framework.CuratorFrameworkFactory.newClient(connectString, retryPolicy);
    }

    @BQConfigProperty("Zookeeper connection String. E.g. 127.0.0.1:2181,127.0.0.1:2182")
    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }
}
