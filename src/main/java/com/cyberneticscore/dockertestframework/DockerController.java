package com.cyberneticscore.dockertestframework;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;
import lombok.extern.log4j.Log4j;

/**
 * Class for communicating with docker
 */
@Log4j
class DockerController {
    protected DockerClient dockerClient;

    private String containerId;

    public DockerController(ContainerConfig containerConfig) {
        init(containerConfig);
    }

    private void init(ContainerConfig containerConfig) {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(containerConfig.getHostPort())
                .build();

        DockerCmdExecFactory dockerCmdExecFactory = new JerseyDockerCmdExecFactory()
                .withReadTimeout(10_000)
                .withConnectTimeout(10_000)
                .withMaxTotalConnections(100)
                .withMaxPerRouteConnections(100);

        dockerClient = DockerClientBuilder.getInstance(config)
                .withDockerCmdExecFactory(dockerCmdExecFactory)
                .build();
    }

    void createContainer(ContainerConfig containerConfig) {
        CreateContainerCmd container = dockerClient
                .createContainerCmd(containerConfig.getImage());

        if (!containerConfig.getEntryPoint().isEmpty()) {
            container.withEntrypoint(containerConfig.getEntryPoint());
        }

        if (!containerConfig.getEnvironmentProperties().isEmpty()) {
            container.withEnv(containerConfig.getEnvironmentProperties());
        }

        if (!containerConfig.getCommandLineArguments().isEmpty()) {
            container.withCmd(containerConfig.getCommandLineArguments());
        }

//        if (!containerConfig.getVolumes().isEmpty()){
//            container=container.withVolumesFrom(containerConfig.getVolumes());
////            Volume volume = new Volume("");
//
////            container.withVolumesFrom()
//       }


        CreateContainerResponse exec = container.exec();

        if (null != exec.getWarnings() && exec.getWarnings().length > 0) {
            LOGGER.warn(exec.getWarnings());
        }

        this.containerId = exec.getId();
    }

    void startContainer() {
        LOGGER.debug("Container Starting: " + this.containerId);

        dockerClient.startContainerCmd(this.containerId).exec();
    }

    Boolean isRunning() {

        Boolean running = dockerClient.inspectContainerCmd(containerId).exec().getState().getRunning();

        LOGGER.debug("Container isRunning? = " + running + "  :" + this.containerId);

        return running;
    }


    void stopContainer() {
        if (isRunning()) {
            LOGGER.info("Trying to stop ID: " + containerId);

            try {

                dockerClient.killContainerCmd(containerId).exec();
            } catch (ConflictException ex) {
                LOGGER.warn(ex);
            }
        }

        Boolean dead = dockerClient.inspectContainerCmd(containerId).exec().getState().getDead();

        if (dead) {
            LOGGER.debug("Container " + containerId + " is DEAD!");
        }

        LOGGER.debug("Container " + containerId + " is stopped!");
    }

    void removeContainer() {
        dockerClient.removeContainerCmd(containerId).withForce(true).exec();

        LOGGER.debug("Container " + containerId + " is removed!");
    }


    protected InspectContainerResponse inspectContainer() {
        return dockerClient.inspectContainerCmd(containerId).exec();
    }

    public String getContainerId() {
        return containerId;
    }



}
