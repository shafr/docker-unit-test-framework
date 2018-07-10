package com.cyberneticscore.dockertestframework;

import com.cyberneticscore.dockertestframework.annotations.*;
import com.spotify.docker.client.exceptions.DockerException;
import org.testng.Assert;
import org.testng.annotations.Test;

@DockerHost("http://192.168.189.132:2375")
@Image("alpine")
@EntryPoint("/bin/sh")
@Environment("key=value")
@Environment("key2=value2")
@CommandLineArgument({"ls", "-ltrh"})
public class ClassAndMethod extends DockerTest{

    @Test()
    @CommandLineArgument({"ls", "-la"})
    public void testCommandLineArgument() throws DockerException, InterruptedException {
        String[] actual = dockerController.inspectContainer().config().cmd().toArray(new String[0]);
        Assert.assertEquals(actual[0], "ls");
        Assert.assertEquals(actual[1], "-la");
    }

    @Test
    @EntryPoint("/bin/ls")
    public void testEntryPoint() throws DockerException, InterruptedException {
        String[] actual = dockerController.inspectContainer().config().entrypoint().toArray(new String[0]);
        Assert.assertEquals(actual[0], "/bin/ls");
    }

    @Test
    @Environment("key3=value3")
    @Environment("key4=value4")
    public void testEnvironment() throws DockerException, InterruptedException {
        String[] actual = dockerController.inspectContainer().config().env().toArray(new String[0]);
        Assert.assertEquals(actual[0], "key=value");
        Assert.assertEquals(actual[1], "key2=value2");
        Assert.assertEquals(actual[2], "key3=value3");
        Assert.assertEquals(actual[3], "key4=value4");
        Assert.assertEquals(actual[4], "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin");
    }

    @Test
    @Environment("key2=value6")
    public void testEnvironmentOverridesPrevious() throws DockerException, InterruptedException {
        String[] actual = dockerController.inspectContainer().config().env().toArray(new String[0]);
        Assert.assertEquals(actual[0], "key=value");
        Assert.assertEquals(actual[1], "key2=value2");
        Assert.assertEquals(actual[2], "key2=value6"); //This is odd though, maybe we should handle this
        Assert.assertEquals(actual[3], "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin");
    }


    @Test
    public void testImage() throws DockerException, InterruptedException {
        String actual = dockerController.inspectContainer().config().image();
        Assert.assertEquals(actual, "alpine");
    }
}
