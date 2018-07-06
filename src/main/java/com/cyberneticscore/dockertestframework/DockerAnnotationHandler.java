package com.cyberneticscore.dockertestframework;

import com.cyberneticscore.dockertestframework.Annotations.*;
import com.cyberneticscore.dockertestframework.Annotations.Image;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

public class DockerAnnotationHandler {
    private ContainerConfig classContainerConfig;
    protected DockerCommandsHandler dockerClient;


    DockerAnnotationHandler() {
        classContainerConfig = new ContainerConfig();
        getHostPortFromAnnotation();
        extractImage();
        extractEnvironmentVariables();
        extactVolumes();
        extractCommandLineArgument();
        extractPorts();
        extractEntryPoint();
    }

    private void extractPorts() {

    }

    private void getHostPortFromAnnotation() {
        if (this.getClass().isAnnotationPresent(DockerHost.class)) {
            //TODO - regex check
            String annotationHostPort = this.getClass().getAnnotation(DockerHost.class).value();
            this.classContainerConfig.setHostPort(annotationHostPort);
        }
    }

    private void extractImage() {
        if (this.getClass().isAnnotationPresent(Image.class)) {
            this.classContainerConfig.setImage(this.getClass().getAnnotation(Image.class).value());
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

        this.classContainerConfig.setVolumes(volumes);
    }

    private void extractEnvironmentVariables() {
        if (this.getClass().isAnnotationPresent(Environment.class)) {
            Environment annotation = this.getClass().getAnnotation(Environment.class);
            this.classContainerConfig.getEnvironmentProperties().add(annotation.value());
        }

        if (this.getClass().isAnnotationPresent(Environments.class)) {
            Environments annotations = this.getClass().getAnnotation(Environments.class);

            for (Environment annotation : annotations.value()) {
                this.classContainerConfig.getEnvironmentProperties().add(annotation.value());
            }
        }
    }

    private void extractCommandLineArgument() {
        if (this.getClass().isAnnotationPresent(CommandLineArgument.class)) {
            CommandLineArgument commandLineArgument = this.getClass().getAnnotation(CommandLineArgument.class);
            this.classContainerConfig.setCommandLineArguments(Arrays.asList(commandLineArgument.value()));
        }
    }

    private void extractEntryPoint() {
        if (this.getClass().isAnnotationPresent(EntryPoint.class)) {
            EntryPoint commandLineArgument = this.getClass().getAnnotation(EntryPoint.class);
            this.classContainerConfig.setEntryPoint(commandLineArgument.value());
        }
    }

    @BeforeMethod
    void BeforeEachTest(Method method) {
        ContainerConfig methodContainerConfig = classContainerConfig.clone();

        handleMethodEnvs(method, methodContainerConfig);

        dockerClient = new DockerCommandsHandler(methodContainerConfig);
        dockerClient.createContainer(methodContainerConfig);

        if (!method.isAnnotationPresent(CreateOnly.class)) {
            dockerClient.startContainer();
        }
    }

    @AfterMethod
    void AfterEachMethod(Method method) {
        if (dockerClient.isRunning()) {
            dockerClient.stopContainer();
        }

        if (!method.isAnnotationPresent(KeepContainer.class)) {
            dockerClient.removeContainer();
        }
    }

    private void handleMethodEnvs(Method method, ContainerConfig methodContainerConfig) {
        if (method.isAnnotationPresent(Environment.class)) {
            Environment annotation = method.getAnnotation(Environment.class);
            methodContainerConfig.getEnvironmentProperties().add(annotation.value());
        }

        if (method.isAnnotationPresent(Environments.class)) {
            Environments annotations = method.getAnnotation(Environments.class);

            for (Environment annotation : annotations.value()) {
                methodContainerConfig.getEnvironmentProperties().add(annotation.value());
            }
        }
    }
}
