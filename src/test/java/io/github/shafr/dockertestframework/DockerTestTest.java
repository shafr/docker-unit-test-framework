package io.github.shafr.dockertestframework;

import com.spotify.docker.client.exceptions.DockerException;
import io.github.shafr.dockertestframework.annotations.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@DockerHost()
@Image("alpine")
public class DockerTestTest extends DockerTest{

    @Test
    @EntryPoint("/bin/ping")
    @CommandLineArgument({"google.com", "-w", "2"})
    public void testExecuteContainerCommand() throws DockerException, InterruptedException {
        String execResult = executeInsideContainer("cat", "/etc/os-release");
        Assert.assertTrue(execResult.contains("Alpine Linux"));
    }

    @Test
    @EntryPoint("/bin/cat")
    @CommandLineArgument("/etc/os-release")
    @KeepContainer
    public void testGetLogs() throws InterruptedException, DockerException {
        String logs = getLog(3000);
        Assert.assertTrue(logs.contains("Alpine Linux"));
    }

    @Test
    @EntryPoint("/bin/ping")
    @CommandLineArgument({"google.com", "-w", "2"})
    public void testWaitForContainerToExit() throws DockerException, InterruptedException {
        boolean stopped = waitForContainerToExit(10_000);
        Assert.assertTrue(stopped);
    }

    @Test()
    @EntryPoint("/bin/ping")
    @CommandLineArgument({"google.com"})
    public void testWaitForContainerNotToExit() throws DockerException, InterruptedException {
        boolean stopped = waitForContainerToExit(2000);
        Assert.assertFalse(stopped);
    }

    @Test
    @EntryPoint("ls")
    @CommandLineArgument("-ltrh")
    public void testGetExitCode() throws DockerException, InterruptedException {
        int exitCode = getExitCode(2000);
        Assert.assertEquals(exitCode, 0);
    }

    @EntryPoint("/bin/ping")
    @CommandLineArgument("google.com")
    public void testGetExitCodeShouldFail() throws DockerException, InterruptedException {
        getExitCode(2000);
    }
}