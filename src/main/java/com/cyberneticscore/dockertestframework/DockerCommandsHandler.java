package com.cyberneticscore.dockertestframework;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class for communicating with docker
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

    void createContainer(ContainerConfig containerConfig) {
        CreateContainerCmd container = dockerClient
                .createContainerCmd(containerConfig.getImage());

        if (!containerConfig.getEntryPoint().isEmpty()){
            container.withEntrypoint(containerConfig.getEntryPoint());
        }

        if (!containerConfig.getEnvironmentProperties().isEmpty()) {
            container.withEnv(containerConfig.getEnvironmentProperties());
        }

        if (!containerConfig.getCommandLineArguments().isEmpty()) {
            container.withEntrypoint(containerConfig.getCommandLineArguments());
        }

//        if (!containerConfig.getVolumes().isEmpty()){
//            container=container.withVolumesFrom(containerConfig.getVolumes());
////            Volume volume = new Volume("");
//
////            container.withVolumesFrom()
//       }


        CreateContainerResponse exec = container.exec();


        System.out.println(Arrays.toString(exec.getWarnings()));

        this.containerId = exec.getId();
    }

    void startContainer() {
        dockerClient.startContainerCmd(this.containerId).exec();
    }

    public Boolean isRunning() {
        return dockerClient.inspectContainerCmd(containerId).exec().getState().getRunning();
    }


    public void stopContainer() {
        if (isRunning()) {
            dockerClient.killContainerCmd(containerId).exec();
        }

        Boolean dead = dockerClient.inspectContainerCmd(containerId).exec().getState().getDead();

        if (dead) {
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

    public void getLogs() {

    }


}
