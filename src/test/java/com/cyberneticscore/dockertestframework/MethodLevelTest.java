package com.cyberneticscore.dockertestframework;

import com.cyberneticscore.dockertestframework.annotations.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@DockerHost("http://192.168.189.132:2375")
@Image("alpine")
public class MethodLevelTest extends DockerTest{

    @Test()
    @CommandLineArgument({"ls", "-ltrh"})
    public void testCommandLineArgument(){
        String[] actual = dockerController.inspectContainer().config().cmd().toArray(new String[0]);
        Assert.assertEquals(actual[0], "ls");
        Assert.assertEquals(actual[1], "-ltrh");
    }

    @Test
    @EntryPoint("/bin/sh")
    public void testEntryPoint(){
        String[] actual = dockerController.inspectContainer().config().entrypoint().toArray(new String[0]);
        Assert.assertEquals(actual[0], "/bin/sh");
    }

    @Test
    @Environment("key=value")
    @Environment("key2=value2")
    public void testEnvironment(){
        String[] actual = dockerController.inspectContainer().config().env().toArray(new String[0]);
        Assert.assertEquals(actual[0], "key=value");
        Assert.assertEquals(actual[1], "key2=value2");
        Assert.assertEquals(actual[2], "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin");
    }

    @Test
    public void testImage(){
        String actual = dockerController.inspectContainer().config().image();
        Assert.assertEquals(actual, "alpine");
    }
}
