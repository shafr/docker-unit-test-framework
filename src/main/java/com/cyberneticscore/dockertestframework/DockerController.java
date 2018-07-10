package com.cyberneticscore.dockertestframework;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.ConflictException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerInfo;
import lombok.extern.log4j.Log4j;

/**
 * Class for communicating with docker
 */
@Log4j
class DockerController {
    protected DockerClient dockerClient;

    private String containerId;

    public DockerController(DockerContainerConfig dockerContainerConfig) {
        init(dockerContainerConfig);
    }

    private void init(DockerContainerConfig dockerContainerConfig) {
        dockerClient = DefaultDockerClient
                .builder()
                .uri(dockerContainerConfig.getHostPort())
                .connectTimeoutMillis(10_000)
                .readTimeoutMillis(30_000)
                .connectionPoolSize(100)
                .build();
    }

    void createContainer(DockerContainerConfig dockerContainerConfig) throws DockerException, InterruptedException {
        final ContainerConfig.Builder builder = ContainerConfig.builder()
//                .hostConfig(dockerContainerConfig.)
                .image(dockerContainerConfig.getImage());

        if (!dockerContainerConfig.getEntryPoint().isEmpty()) {
            builder.entrypoint(dockerContainerConfig.getEntryPoint());
        }

        if (!dockerContainerConfig.getEnvironmentProperties().isEmpty()) {
            builder.env(dockerContainerConfig.getEnvironmentProperties());
        }

        if (!dockerContainerConfig.getCommandLineArguments().isEmpty()) {
            builder.cmd(dockerContainerConfig.getCommandLineArguments());
        }

//        if (!dockerContainerConfig.getVolumes().isEmpty()){
//            container=container.withVolumesFrom(dockerContainerConfig.getVolumes());
////            Volume volume = new Volume("");
//
////            container.withVolumesFrom()
//       }

        final ContainerCreation creation = dockerClient.createContainer(builder.build());

        this.containerId = creation.id();
    }

    void startContainer() throws DockerException, InterruptedException {
        LOGGER.debug("Container Starting: " + this.containerId);

        dockerClient.startContainer(this.containerId);
    }

    Boolean isRunning() throws DockerException, InterruptedException {

        Boolean running = dockerClient.inspectContainer(containerId).state().running();

        LOGGER.debug("Container isRunning? = " + running + "  :" + this.containerId);

        return running;
    }


    void stopContainer() throws DockerException, InterruptedException {
        if (isRunning()) {
            LOGGER.info("Trying to stop ID: " + containerId);

            try {

                dockerClient.killContainer(containerId);
            } catch (ConflictException ex) {
                LOGGER.warn(ex);
            }
        }

        Boolean dead = dockerClient.inspectContainer(containerId).state().oomKilled();

        if (dead) {
            LOGGER.debug("Container " + containerId + " is DEAD!");
        }

        LOGGER.debug("Container " + containerId + " is stopped!");
    }

    void removeContainer() {
        try {
            dockerClient.removeContainer(containerId, DockerClient.RemoveContainerParam.forceKill());
            LOGGER.debug("Container " + containerId + " is removed!");
        } catch (DockerException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }


    protected ContainerInfo inspectContainer() {
        try {
            return dockerClient.inspectContainer(containerId);
        } catch (DockerException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public String getContainerId() {
        return containerId;
    }
}
