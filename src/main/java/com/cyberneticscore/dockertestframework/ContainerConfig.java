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
        try {
            return (ContainerConfig)super.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }

        return null; //TODO - proper cloning
    }
}
