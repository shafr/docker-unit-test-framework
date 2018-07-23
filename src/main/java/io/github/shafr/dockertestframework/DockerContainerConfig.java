package io.github.shafr.dockertestframework;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
class DockerContainerConfig implements Cloneable{
    private String hostPort = "";
    private String image = "";
    private String entryPoint = "";
    private List<String> environmentProperties = new ArrayList<>(0);
    private HashMap<String, String> volumes = new HashMap<>(0);
    private List<String> commandLineArguments = new ArrayList<>(0);

    public DockerContainerConfig clone() {

       DockerContainerConfig dockerContainerConfig = new DockerContainerConfig();
       dockerContainerConfig.hostPort = this.hostPort;
       dockerContainerConfig.image = this.image;
       dockerContainerConfig.entryPoint = this.entryPoint;
       dockerContainerConfig.environmentProperties.addAll(this.environmentProperties);
       dockerContainerConfig.volumes.putAll(this.volumes);
       dockerContainerConfig.commandLineArguments.addAll(this.commandLineArguments);

       return dockerContainerConfig;
    }
}
