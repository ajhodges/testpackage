package com.deloittedigital.testpackage.test;

import com.deloittedigital.testpackage.TestSequencer;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.Request;

import java.io.IOException;
import java.util.Map;

import static com.deloittedigital.testpackage.VisibleAssertions.assertEquals;

/**
 * Created by richardnorth on 20/12/2013.
 */
public class TestSequencerTest {

    @Test
    public void testSimpleContains() throws IOException {
        Request request = new TestSequencer().sequenceTests("com.deloittedigital.testpackage.runnertest.simpletests");

        assertEquals("the request contains the right number of test methods", 2, request.getRunner().testCount());
        assertEquals("the request has the test methods in lexicographic order (1/2)", "testTrue1(com.deloittedigital.testpackage.runnertest.simpletests.SimpleTest)", request.getRunner().getDescription().getChildren().get(0).getChildren().get(0).getDisplayName());
        assertEquals("the request has the test methods in lexicographic order (2/2)", "testTrue2(com.deloittedigital.testpackage.runnertest.simpletests.SimpleTest)", request.getRunner().getDescription().getChildren().get(0).getChildren().get(1).getDisplayName());

    }

    @Test
    public void testRecentFailurePrioritisation() throws IOException {

        Map<String, Integer> runsSinceLastFailures = Maps.newHashMap();
        runsSinceLastFailures.put("com.deloittedigital.testpackage.runnertest.failureprioritisationtests.zzz_JustFailedTest", 0);
        runsSinceLastFailures.put("testTrue(com.deloittedigital.testpackage.runnertest.failureprioritisationtests.zzz_JustFailedTest)", 0);

        Request request = new TestSequencer().sequenceTests(runsSinceLastFailures, "com.deloittedigital.testpackage.runnertest.failureprioritisationtests");

        assertEquals("the request contains the right number of test methods", 3, request.getRunner().testCount());
        assertEquals("the first test method is one which has faield most recently", "testTrue(com.deloittedigital.testpackage.runnertest.failureprioritisationtests.zzz_JustFailedTest)", request.getRunner().getDescription().getChildren().get(0).getChildren().get(0).getDisplayName());
        assertEquals("the second test method is one which hasn't ever failed but belongs to the same class as the first", "testThatHasNotFailed(com.deloittedigital.testpackage.runnertest.failureprioritisationtests.zzz_JustFailedTest)", request.getRunner().getDescription().getChildren().get(0).getChildren().get(1).getDisplayName());
        assertEquals("the last test method is one which has never failed and belongs to class with no historical failures", "testTrue(com.deloittedigital.testpackage.runnertest.failureprioritisationtests.aaa_NoRecentFailuresTest)", request.getRunner().getDescription().getChildren().get(1).getChildren().get(0).getDisplayName());

    }
}
