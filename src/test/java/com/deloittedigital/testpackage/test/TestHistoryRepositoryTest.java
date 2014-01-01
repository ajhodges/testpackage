package com.deloittedigital.testpackage.test;

import com.deloittedigital.testpackage.sequencing.TestHistoryRepository;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.deloittedigital.testpackage.VisibleAssertions.assertEquals;

/**
 * Created by richardnorth on 01/01/2014.
 */
public class TestHistoryRepositoryTest {

    @Test
    public void simpleStorageTest() throws IOException {
        TestHistoryRepository repository = new TestHistoryRepository("src/test/resources/historysample1.txt");

        Map<String, Integer> runsSinceLastFailures = repository.getRunsSinceLastFailures();
        assertEquals("a just-failed test class is marked as such", runsSinceLastFailures.get("com.deloittedigital.testpackage.runnertest.failureprioritisationtests.zzz_JustFailedTest"), 0);
        assertEquals("a just-failed test method is marked as such", runsSinceLastFailures.get("testTrue(com.deloittedigital.testpackage.runnertest.failureprioritisationtests.zzz_JustFailedTest)"), 0);
    }

    @Test
    public void testIncrementOnSave() throws IOException {

        File tempFile = File.createTempFile("testhistory", ".txt");
        Files.copy(new File("src/test/resources/historysample2.txt"), tempFile);

        TestHistoryRepository repository = new TestHistoryRepository(tempFile.getAbsolutePath());
        Map<String, Integer> runsSinceLastFailures = repository.getRunsSinceLastFailures();
        assertEquals("a just-failed test class has a count of zero", runsSinceLastFailures.get("ClassName"), 0);
        assertEquals("a just-failed test method has a count of zero", runsSinceLastFailures.get("methodName(ClassName)"), 0);

        repository.markFailure("JustFailedClass", "justFailedMethod(JustFailedClass)");

        repository.save();

        repository = new TestHistoryRepository(tempFile.getAbsolutePath());
        runsSinceLastFailures = repository.getRunsSinceLastFailures();
        assertEquals("a recently-failed test class has an incremented count", runsSinceLastFailures.get("ClassName"), 1);
        assertEquals("a recently-failed test method has an incremented count", runsSinceLastFailures.get("methodName(ClassName)"), 1);
        assertEquals("a just-failed test class has a count of zero", runsSinceLastFailures.get("JustFailedClass"), 0);
        assertEquals("a just-failed test method has a count of zero", runsSinceLastFailures.get("justFailedMethod(JustFailedClass)"), 0);

    }
}
