package com.cyberneticscore.dockertestframework;

import com.cyberneticscore.dockertestframework.Annotations.*;
import org.testng.annotations.Test;

@DockerHost("tcp://shadm01-co7:2375")
@Image("alpine")
public class Test2 extends DockerAnnotationHandler {
    @Test
    public void Test1() {

    }

    @CommandLineArgument(command = "ps", arguments = "etc") //TODO - multi-string cool looking
    @Test
    public void Test2(){

    }
}
