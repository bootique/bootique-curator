package com.nhl.bootique.curator;

import com.nhl.bootique.test.junit.BQModuleProviderChecker;
import org.junit.Test;

public class CuratorModuleProviderTest {

    @Test
    public void testPresentInJar() {
        BQModuleProviderChecker.testPresentInJar(CuratorModuleProvider.class);
    }
}
