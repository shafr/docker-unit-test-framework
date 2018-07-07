package com.cyberneticscore.dockertestframework;

import com.cyberneticscore.dockertestframework.annotations.*;
import com.github.dockerjava.api.exception.DockerClientException;
import org.testng.Assert;
import org.testng.annotations.Test;

@DockerHost
@Image("alpine")
public class DockerTestTest extends DockerTest{

    @Test
    public void testExecuteContainerCommand() {

    }

    @Test
    @EntryPoint("/bin/cat")
    @CommandLineArgument("/etc/os-release")
    @KeepContainer
    public void testGetLogs() throws InterruptedException {
        String logs = getLog(3000);
        Assert.assertTrue(logs.contains("Alpine Linux"));
    }

    @Test
    @EntryPoint("/bin/ping")
    @CommandLineArgument({"google.com", "-w", "2"})
    public void testWaitForContainerToExit() {
        boolean stopped = waitForContainerToExit(4000);
        Assert.assertTrue(stopped);
    }

    @Test()
    @EntryPoint("/bin/ping")
    @CommandLineArgument({"google.com"})
    public void testWaitForContainerNotToExit() {
        boolean stopped = waitForContainerToExit(2000);
        Assert.assertFalse(stopped);
    }

    @Test
    @EntryPoint("ls")
    @CommandLineArgument("-ltrh")
    public void testGetExitCode() {
        int exitCode = getExitCode(2000);
        Assert.assertEquals(exitCode, 0);
    }

    @Test(expectedExceptions = DockerClientException.class)
    @EntryPoint("/bin/ping")
    @CommandLineArgument("google.com")
    public void testGetExitCodeShouldFail() {
        getExitCode(2000);
    }
}