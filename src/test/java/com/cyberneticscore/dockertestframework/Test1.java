package com.cyberneticscore.dockertestframework;

import com.cyberneticscore.dockertestframework.Annotations.*;
import com.github.dockerjava.api.command.InspectContainerResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

@DockerHost("tcp:/localhost:2375")
@Image("alpine")
@Volume(local = "Vol1", remote = "drv")
@Volume(local = "Vol2", remote = "drv2")
public class Test1 extends DockerAnnotationHandler {
    @Environment("EM_HOST_PORT=http://tas-cz-n101:8081")
    @Environment("abc=123")
    @Test
    @KeepContainer
    public void Test_1() {
        System.out.println("FROM TEST");
        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainer();
        Assert.assertEquals(inspectContainerResponse.getConfig().getEnv().length, 3);
        inspectContainerResponse.getState().getExitCode();
    }

    @CommandLineArgument("etc") //TODO - multi-string cool looking
    @Test
    public void Test_2() {
        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainer();
        inspectContainerResponse.getVolumes();
        Assert.assertEquals(inspectContainerResponse.getConfig().getEnv().length, 3);
        Assert.assertEquals(inspectContainerResponse.getConfig().getImage(), "alpine");
    }

    @EntryPoint("bash")
    @Test
    public void Test3() {
//        System.out.println(dockerHostPort);
    }


    @BeforeMethod
    public void before(Method method) {
        System.out.println("IN HERE");
    }

    //TODO - run not from tests folder but from normal folder

}
