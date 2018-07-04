package com.cyberneticscore.dockertestframework;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class ContainerConfig implements Cloneable{
    private String hostPort;
    private String image;
    private List<String> environmentProperties = new ArrayList<>();
    private HashMap<String, String> volumes;
    private String commandLineCommand;
    private List<String> commandLineArguments = new ArrayList<>();

    public ContainerConfig clone() {
        try {
            return (ContainerConfig)super.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }

        return null; //TODO - proper cloning
    }
}
