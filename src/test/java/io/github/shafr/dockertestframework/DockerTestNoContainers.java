package io.github.shafr.dockertestframework;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DockerTestNoContainers {

    @Test
    public void testScanForExceptions() throws IOException, URISyntaxException {
        Path pathToFile = Paths.get(this.getClass().getResource("/logs/exceptionsForScanner.log").toURI());
        String logFile = new String(Files.readAllBytes(pathToFile));

        DockerTest dockerTest = new DockerTestTest();
        List<String> stackTraces = dockerTest.scanForExceptions(logFile);

        Assert.assertEquals(stackTraces.size(), 3);

        String[] firstException = stackTraces.get(0).split("\r\n");
        Assert.assertEquals(firstException[0], "java.lang.ClassNotFoundException: com.something.something");
        Assert.assertEquals(firstException[firstException.length-1], "at org.eclipse.core.runtime.adaptor.EclipseStarter.run(EclipseStarter.java:179)");
        Assert.assertEquals(firstException.length, 8);

        String[] secondException = stackTraces.get(1).split("\r\n");
        Assert.assertEquals(secondException[0], "java.lang.NullPointerException:");
        Assert.assertEquals(secondException[secondException.length-1], "at java.lang.Thread.run(Unknown Source)");
        Assert.assertEquals(secondException.length, 14);

        String[] thirdException = stackTraces.get(2).split("\r\n");
        Assert.assertEquals(thirdException[0], "org.springframework.beans.factory.BeanCreationException: java.lang.IllegalStateException: SomethingSomething");
        Assert.assertEquals(thirdException[thirdException.length-1], "at java.lang.reflect.Method.invoke(Unknown Source)");
        Assert.assertEquals(thirdException.length, 8);
    }
}
