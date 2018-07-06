package com.cyberneticscore.dockertestframework;

import com.cyberneticscore.dockertestframework.Annotations.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@DockerHost()
@Image("alpine")
public class MethodLevelTest extends DockerTest{

    @CommandLineArgument({"ls", "-ltrh"})
    @Test()
    public void TestCommandLineArgument(){
        dockerClient.getLogs();
    }

    @Test
    @EntryPoint("/bin/sh")
    @Environment("key=value")
    @Environment("key2=value2")
    public void testEnvironment(){
        String[] actual = dockerClient.inspectContainer().getConfig().getEnv();
        Assert.assertEquals(actual[0], "key=value");
        Assert.assertEquals(actual[1], "key2=value2");
        Assert.assertEquals(actual[2], "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin");
    }

    @Test
    public void testImage(){
        String actual = dockerClient.inspectContainer().getConfig().getImage();
        Assert.assertEquals(actual, "alpine");
    }
}
