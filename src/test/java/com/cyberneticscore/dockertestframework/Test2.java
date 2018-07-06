package com.cyberneticscore.dockertestframework;

import com.cyberneticscore.dockertestframework.Annotations.CommandLineArgument;
import com.cyberneticscore.dockertestframework.Annotations.DockerHost;
import com.cyberneticscore.dockertestframework.Annotations.Image;
import org.testng.annotations.Test;

@DockerHost("tcp://localhost:2375")
@Image("alpine")
public class Test2 extends DockerTest {
    @Test
    public void Test1() {

    }

    @CommandLineArgument("etc") //TODO - multi-string cool looking
    @Test
    public void Test2(){

    }
}
