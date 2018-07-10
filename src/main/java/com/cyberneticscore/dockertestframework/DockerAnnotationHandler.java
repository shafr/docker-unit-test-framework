package com.cyberneticscore.dockertestframework;

import com.cyberneticscore.dockertestframework.annotations.*;
import com.spotify.docker.client.exceptions.DockerException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

class DockerAnnotationHandler {
    private DockerContainerConfig classDockerContainerConfig;
    protected DockerController dockerController;

    DockerAnnotationHandler() {
        classDockerContainerConfig = new DockerContainerConfig();
        getHostPortFromAnnotation();
        extractImage();
        extractEnvironmentVariables();
        extactVolumes();
        extractCommandLineArgument();
        extractEntryPoint();
    }

    private void getHostPortFromAnnotation() {
        if (this.getClass().isAnnotationPresent(DockerHost.class)) {
            //TODO - regex check
            String annotationHostPort = this.getClass().getAnnotation(DockerHost.class).value();
            this.classDockerContainerConfig.setHostPort(annotationHostPort);
        }
    }

    private void extractImage() {
        if (this.getClass().isAnnotationPresent(Image.class)) {
            this.classDockerContainerConfig.setImage(this.getClass().getAnnotation(Image.class).value());
        }
    }

    private void extactVolumes() {
        HashMap<String, String> volumes = new HashMap<>(0);
        //TODO - Validation of correct path?
        if (this.getClass().isAnnotationPresent(Volume.class)) {

            Volume singleVolume = this.getClass().getClass().getAnnotation(Volume.class);
            volumes.put(singleVolume.local(), singleVolume.remote());
        }

        if (this.getClass().isAnnotationPresent(Volumes.class)) {
            Volumes multipleVolumes = this.getClass().getAnnotation(Volumes.class);
            for (Volume singleVolume : multipleVolumes.value()) {
                volumes.put(singleVolume.local(), singleVolume.remote());
            }
        }

        this.classDockerContainerConfig.setVolumes(volumes);
    }

    private void extractEnvironmentVariables() {
        if (this.getClass().isAnnotationPresent(Environment.class)) {
            Environment annotation = this.getClass().getAnnotation(Environment.class);
            this.classDockerContainerConfig.getEnvironmentProperties().add(annotation.value());
        }

        if (this.getClass().isAnnotationPresent(Environments.class)) {
            Environments annotations = this.getClass().getAnnotation(Environments.class);

            for (Environment annotation : annotations.value()) {
                this.classDockerContainerConfig.getEnvironmentProperties().add(annotation.value());
            }
        }
    }

    private void extractCommandLineArgument() {
        if (this.getClass().isAnnotationPresent(CommandLineArgument.class)) {
            CommandLineArgument commandLineArgument = this.getClass().getAnnotation(CommandLineArgument.class);
            this.classDockerContainerConfig.setCommandLineArguments(Arrays.asList(commandLineArgument.value()));
        }
    }

    private void extractEntryPoint() {
        if (this.getClass().isAnnotationPresent(EntryPoint.class)) {
            EntryPoint commandLineArgument = this.getClass().getAnnotation(EntryPoint.class);
            this.classDockerContainerConfig.setEntryPoint(commandLineArgument.value());
        }
    }

    private DockerContainerConfig handleMethodAttributes(Method method, DockerContainerConfig classDockerContainerConfig) {
        DockerContainerConfig methodContainerConfg = classDockerContainerConfig.clone();

        if (method.isAnnotationPresent(Environment.class)) {
            Environment annotation = method.getAnnotation(Environment.class);
            methodContainerConfg.getEnvironmentProperties().add(annotation.value());
        }

        if (method.isAnnotationPresent(Environments.class)) {
            Environments annotations = method.getAnnotation(Environments.class);

            for (Environment annotation : annotations.value()) {
                methodContainerConfg.getEnvironmentProperties().add(annotation.value());
            }
        }

        if (method.isAnnotationPresent(CommandLineArgument.class)) {
            CommandLineArgument commandLineArgument = method.getAnnotation(CommandLineArgument.class);
            methodContainerConfg.setCommandLineArguments(Arrays.asList(commandLineArgument.value()));
        }

        if (method.isAnnotationPresent(EntryPoint.class)) {
            EntryPoint commandLineArgument = method.getAnnotation(EntryPoint.class);
            methodContainerConfg.setEntryPoint(commandLineArgument.value());
        }

        return methodContainerConfg;
    }

    @BeforeMethod
    void beforeEachTest(Method method) throws DockerException, InterruptedException {
        DockerContainerConfig methodDockerContainerConfig = handleMethodAttributes(method, classDockerContainerConfig);

        dockerController = new DockerController(methodDockerContainerConfig);
        dockerController.createContainer(methodDockerContainerConfig);

        if (!method.isAnnotationPresent(CreateOnly.class)) {
            dockerController.startContainer();
        }
    }

    @AfterMethod
    void afterEachTest(Method method) throws DockerException, InterruptedException {
        if (dockerController.isRunning()) {
            dockerController.stopContainer();
        }

        if (!method.isAnnotationPresent(KeepContainer.class)) {
            dockerController.removeContainer();
        }
    }
}
