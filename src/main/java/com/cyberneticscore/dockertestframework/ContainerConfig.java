package com.cyberneticscore.dockertestframework;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
class ContainerConfig implements Cloneable{
    private String hostPort = "";
    private String image = "";
    private String entryPoint = "";
    private List<String> environmentProperties = new ArrayList<>(0);
    private HashMap<String, String> volumes = new HashMap<>(0);
    private List<String> commandLineArguments = new ArrayList<>(0);

    public ContainerConfig clone() {

       ContainerConfig containerConfig = new ContainerConfig();
       containerConfig.hostPort = this.hostPort;
       containerConfig.image = this.image;
       containerConfig.entryPoint = this.entryPoint;
       containerConfig.environmentProperties.addAll(this.environmentProperties);
       containerConfig.volumes.putAll(this.volumes);
       containerConfig.commandLineArguments.addAll(this.commandLineArguments);

       return containerConfig;
    }
}
