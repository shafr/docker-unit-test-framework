package com.cyberneticscore.dockertestframework;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;

import java.util.Arrays;

/**
 * Class for communicating with docker
 * Created by shadm01 on 22.11.16.
 */
public class DockerCommandsHandler {
    private DockerClient dockerClient;

    private String containerId;

    public DockerCommandsHandler(ContainerConfig containerConfig) {
        init(containerConfig);
    }

    private void init(ContainerConfig containerConfig) {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(containerConfig.getHostPort())
                .build();

        DockerCmdExecFactory dockerCmdExecFactory = new JerseyDockerCmdExecFactory()
                .withReadTimeout(1000)
                .withConnectTimeout(1000)
                .withMaxTotalConnections(100)
                .withMaxPerRouteConnections(10);

        dockerClient = DockerClientBuilder.getInstance(config)
                .withDockerCmdExecFactory(dockerCmdExecFactory)
                .build();
    }

    void createAndStartContainer(ContainerConfig containerConfig) {
        CreateContainerResponse exec = dockerClient
                .createContainerCmd(containerConfig.getImage())
                .withEnv(containerConfig.getEnvironmentProperties())
                .exec();


        System.out.println(Arrays.toString(exec.getWarnings()));

        this.containerId = exec.getId();

        dockerClient.startContainerCmd(this.containerId).exec();

    }

    public void stopContainer() {

        boolean running = dockerClient.inspectContainerCmd(containerId).exec().getState().getRunning();

        if (running){
            dockerClient.killContainerCmd(containerId).exec();
        }

        Boolean dead = dockerClient.inspectContainerCmd(containerId).exec().getState().getDead();

        if (dead){
            System.out.println("DEAD!");
        }

        System.out.println("Container is stopped!");
    }

    public void removeContainer() {
        dockerClient.removeContainerCmd(containerId).withForce(true).exec();

        System.out.println("Container is removed!");
    }


    protected InspectContainerResponse inspectContainer() {
        return dockerClient.inspectContainerCmd(containerId).exec();
    }

//    public List<DisplayContainer> getContainers(){
//        List<Container> containers = dockerClient.listContainersCmd().exec();
//
//
//        List<DisplayContainer> displayContainers = new ArrayList<>();
//        for (Container container : containers){
//            DisplayContainer displayContainer =
//                    new DisplayContainer(
//                            container.getNames(),
//                            container.getCommand(),
//                            container.getImage(),
//                            container.getId(),
//                            container.getPorts(),
//                            container.getLabels(),
//                            container.getStatus());
//            displayContainers.add(displayContainer);
//        }
//        return displayContainers;
//    }

    public void getLogs(String containerId) {
        LogContainerCmd logContainerCmd = dockerClient.logContainerCmd(containerId);
    }


}
