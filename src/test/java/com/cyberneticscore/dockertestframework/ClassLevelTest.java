package com.cyberneticscore.dockertestframework;

import com.cyberneticscore.dockertestframework.Annotations.*;
import com.github.dockerjava.api.model.ExposedPort;
import org.bouncycastle.util.Arrays;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

@DockerHost()
@EntryPoint("/bin/someentrypoint")
@Environment("key=value")
@Environment("key2=value2")
@Image("alpine")
@CommandLineArgument({"ls", "-", "ltrh"})

public class ClassLevelTest extends DockerTest{
    @Test
    public void TestHost(){
        //I haven't found how to check which host you are connected to - if it would fail, other tests would not start.
    }

    @Test
    public void testEntryPoint(){
        String[] actual = dockerClient.inspectContainer().getConfig().getEntrypoint();
        Assert.assertEquals(actual, "");
    }

    @Test
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

    @Test
    public void testPorts(){
        ExposedPort[] actual = dockerClient.inspectContainer().getConfig().getExposedPorts();
        Assert.assertNull(actual);
    }
}
