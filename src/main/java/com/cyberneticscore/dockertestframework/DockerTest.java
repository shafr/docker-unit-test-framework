package com.cyberneticscore.dockertestframework;

import com.github.dockerjava.api.command.InspectContainerResponse.ContainerState;
import com.github.dockerjava.core.command.WaitContainerResultCallback;
import lombok.extern.log4j.Log4j;

import java.util.concurrent.TimeUnit;

@Log4j
public class DockerTest extends DockerAnnotationHandler {
    public String getLog(int timeoutInMs) throws InterruptedException {
        LogCollectorCallback logContainerResultCallback = new LogCollectorCallback();

        dockerController.dockerClient.logContainerCmd(dockerController.getContainerId())
                .withStdOut(true).withStdErr(true)
                .withTailAll()
                .exec(logContainerResultCallback).awaitCompletion();

        logContainerResultCallback.awaitCompletion(timeoutInMs, TimeUnit.MILLISECONDS);

        return logContainerResultCallback.toString();
    }

    public boolean waitForContainerToExit(int timeoutInMs) {
        while (timeoutInMs > 0) {
            ContainerState state = dockerController.dockerClient.inspectContainerCmd(dockerController.getContainerId()).exec().getState();
            if (!state.getRunning()){
                LOGGER.info("Container is not running");
                return true;
            }

            timeoutInMs -= 1000;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                LOGGER.error(ex);
            }
        }

        return false;

    }

    public int getExitCode(int timeoutInMs) {
        return dockerController.dockerClient.waitContainerCmd(dockerController.getContainerId())
                .exec(new WaitContainerResultCallback())
                .awaitStatusCode(timeoutInMs, TimeUnit.MILLISECONDS);
    }

}
