package io.github.shafr.dockertestframework;

import io.github.shafr.dockertestframework.annotations.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@DockerHost()
@Image("alpine")
@EntryPoint("/bin/sh")
@Environment("key=value")
@Environment("key2=value2")
@CommandLineArgument({"ls", "-ltrh"})
@PullLatest
public class ClassLevelTest extends DockerTest{
    @Test
    public void tstHost(){
        //I haven't found how to check which host you are connected to - if it would fail, other tests would not start.
    }

    @Test
    public void testImage(){
        String actual = dockerController.inspectContainer().image();
        Assert.assertEquals(actual, "alpine");
    }

    @Test
    public void testEntryPoint(){
        String[] actual = dockerController.inspectContainer().entrypoint().toArray(new String[0]);
        Assert.assertEquals(actual[0], "/bin/sh");
    }

    @Test
    public void testEnvironment(){
        String[] actual = dockerController.inspectContainer().env().toArray(new String[0]);
        Assert.assertEquals(actual[0], "key=value");
        Assert.assertEquals(actual[1], "key2=value2");
        Assert.assertEquals(actual[2], "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin");
    }

    @Test
    public void testCommandLineArguments(){
        String[] actual = dockerController.inspectContainer().cmd().toArray(new String[0]);
        Assert.assertEquals(actual[0], "ls");
        Assert.assertEquals(actual[1], "-ltrh");
    }

    @Test
    public void testPullLatest(){
        //TODO - not sure how to test - need to find the best way
        String imageName = dockerController.inspectContainer().image();
        System.out.println(imageName);
    }
}
