package com.cyberneticscore.dockertestframework;

import com.cyberneticscore.dockertestframework.annotations.*;
import com.spotify.docker.client.exceptions.DockerException;
import org.testng.Assert;
import org.testng.annotations.Test;

@DockerHost("http://192.168.189.132:2375")
@Image("alpine")
public class DockerTestTest extends DockerTest{

    @Test
    public void testExecuteContainerCommand() {

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
        boolean stopped = waitForContainerToExit(4000);
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