package io.github.shafr.dockertestframework;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerState;
import com.spotify.docker.client.messages.ExecCreation;
import com.spotify.docker.client.messages.TopResults;
import lombok.extern.log4j.Log4j;

import static com.spotify.docker.client.DockerClient.LogsParam;

@Log4j
public class DockerTest extends DockerAnnotationHandler {
    /**
     * Exposes information of container provided by Docker API
     *
     * @return Info about the container
     */
    public ContainerConfig inspectContainer() {
        return dockerController.inspectContainer();
    }

    /**
     * @param psArguments the arguments to pass to <code>ps</code>
     *                    *               inside the container, e.g., <code>"-ef"</code>
     * @return the titles and process list for the container
     * @throws DockerException      if a server error occurred (500)
     * @throws InterruptedException If the thread is interrupted
     */
    public TopResults getRunningProcesses(String psArguments) throws DockerException, InterruptedException {
        return dockerController.dockerClient.topContainer(dockerController.getContainerId(), psArguments);
    }

    /**
     * Returns contents of file that is located inside container
     *
     * @param filePath - absolute path to a file
     * @return contents of file
     * @throws InterruptedException If the thread is interrupted
     * @throws DockerException      if a server error occurred (500)
     */
    public String getFileContents(String filePath) throws InterruptedException, DockerException {
        ExecCreation execCreation = dockerController.dockerClient.execCreate(
                dockerController.getContainerId(),
                new String[]{"cat", filePath},
                DockerClient.ExecCreateParam.attachStderr(),
                DockerClient.ExecCreateParam.attachStdout()
        );
        String log;

        try (final LogStream stream = dockerController.dockerClient.execStart(execCreation.id())) {
            log = stream.readFully();
        }

        return log;
    }

    /**
     * Waits until log line appears in logs
     *
     * @param textToMatch - line to match in logs
     * @param timeoutInMs - timeout in ms to wait for
     * @return true if line was found during timeout
     * @throws DockerException      if a server error occurred (500)
     * @throws InterruptedException If the thread is interrupted
     */
    public boolean waitForContainerLogLine(String textToMatch, final int timeoutInMs) throws DockerException, InterruptedException {
        //TODO - find lambda or some universal method for this timed operations
        int waitTime = 0;
        String logs;

        while (timeoutInMs > waitTime) {
            try (LogStream stream = dockerController.dockerClient.logs(
                    dockerController.getContainerId(),
                    LogsParam.stdout(),
                    LogsParam.stderr())) {
                logs = stream.readFully();

                if (logs.contains(textToMatch)) {
                    LOGGER.info("Line was found in: " + waitTime + " ms");
                    return true;
                }
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

    /**
     * @param cmd - command with arguments to be executed
     * @return - response that was generated by command
     * @throws InterruptedException if a server error occurred (500)
     * @throws DockerException      If the thread is interrupted
     */
    public String executeInsideContainer(String... cmd) throws InterruptedException, DockerException {
        ExecCreation execCreation = dockerController.dockerClient.execCreate(
                dockerController.getContainerId(),
                cmd,
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

    /**
     * Returns container log contents
     *
     * @return Log from container
     * @throws InterruptedException If the thread is interrupted
     * @throws DockerException      if a server error occurred (500)
     */
    public String getContainerLog() throws InterruptedException, DockerException {

        final String logs;
        try (LogStream stream = dockerController.dockerClient.logs(
                dockerController.getContainerId(),
                LogsParam.stdout(),
                LogsParam.stderr())) {
            logs = stream.readFully();
        }

        return logs;
    }

    /**
     * Waits container termination
     *
     * @param timeoutInMs timeout in ms
     * @return boolean if container was terminated during time
     * @throws DockerException      if a server error occurred (500)
     * @throws InterruptedException If the thread is interrupted
     */
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

    /**
     * Returns exit code for container
     *
     * @param timeoutInMs - timeout in ms
     * @return exit code
     * @throws DockerException      if a server error occurred (500)
     * @throws InterruptedException If the thread is interrupted
     */
    public int getExitCode(int timeoutInMs) throws DockerException, InterruptedException {
        //TODO - implement own waiting
//        dockerController.dockerClient.waitContainer(dockerController.getContainerId()).
        return dockerController.dockerClient.waitContainer(dockerController.getContainerId()).statusCode();

        //TODO - TIMEOUT is needed here
    }
}
