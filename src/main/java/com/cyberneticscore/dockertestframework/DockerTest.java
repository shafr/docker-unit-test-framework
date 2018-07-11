package com.cyberneticscore.dockertestframework;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerState;
import com.spotify.docker.client.messages.ExecCreation;
import lombok.extern.log4j.Log4j;

import static com.spotify.docker.client.DockerClient.LogsParam;

@Log4j
public class DockerTest extends DockerAnnotationHandler  {
    public String executeInsideContainer(String... cmd) throws InterruptedException, DockerException{
        ExecCreation execCreation = dockerController.dockerClient.execCreate(dockerController.getContainerId(), cmd,
                DockerClient.ExecCreateParam.attachStderr(),
                DockerClient.ExecCreateParam.attachStdout()
        );
        String log;

        try (final LogStream stream = dockerController.dockerClient.execStart(execCreation.id())) {
           log = stream.readFully();
        }

        //TODO - use state as well
//        final ExecState state = dockerController.dockerClient.execInspect(execCreation.id());

        return log;
    }


    public String getLog(int timeoutInMs) throws InterruptedException, DockerException {

        final String logs;
        try (LogStream stream = dockerController.dockerClient.logs(
                dockerController.getContainerId(),
                LogsParam.stdout(),
                LogsParam.stderr())) {
            logs = stream.readFully();
        }

        return logs;
    }

    public boolean waitForContainerToExit(final int timeoutInMs) throws DockerException, InterruptedException {
        //TODO - find lambda or some universal method for this timed operations
        int waitTime = 0;
        while (timeoutInMs > waitTime) {
            ContainerState state = dockerController.dockerClient.inspectContainer(dockerController.getContainerId()).state();
            if (!state.running()) {
                LOGGER.debug("Container is not running, wait time: " + waitTime);
                return true;
            }

            waitTime += 100;

            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                LOGGER.error(ex);
            }
        }

        return false;

    }

    public int getExitCode(int timeoutInMs) throws DockerException, InterruptedException {
        //TODO - implement own waiting
//        dockerController.dockerClient.waitContainer(dockerController.getContainerId()).
        return dockerController.dockerClient.waitContainer(dockerController.getContainerId()).statusCode();

        //TODO - TIMEOUT is needed here
    }

}
