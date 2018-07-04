package com.cyberneticscore.dockertestframework;

import com.cyberneticscore.dockertestframework.Annotations.*;
import com.cyberneticscore.dockertestframework.Annotations.Image;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

public class DockerAnnotationHandler extends DockerUtilMethods {
    private ContainerConfig classContainerConfig;
    protected DockerCommandsHandler dockerClient;


    DockerAnnotationHandler() {
        classContainerConfig = new ContainerConfig();
        getHostPortFromAnnotation(this.getClass());
        extractImage();
        extractEnvironmentVariables(this.getClass());
        extactVolumes(this.getClass());
        extractCommandLineArgument(this.getClass());
        System.out.println("Constructor");
    }

    private void getHostPortFromAnnotation(Class classs) {
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

    private void extactVolumes(Class classs) {
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

    private void extractEnvironmentVariables(Class classs) {
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

    private void extractCommandLineArgument(Class classs) {
        if (this.getClass().isAnnotationPresent(CommandLineArgument.class)) {
            CommandLineArgument commandLineArgument = this.getClass().getAnnotation(CommandLineArgument.class);
            this.classContainerConfig.setCommandLineCommand(commandLineArgument.command());
            this.classContainerConfig.setCommandLineArguments(Arrays.asList(commandLineArgument.arguments()));
        }
    }

//    @BeforeClass
//    public void beforeEachClass() {
//        classContainerConfig = new ContainerConfig();
//        System.out.println("Before Class");
//    }

    @BeforeMethod
    void BeforeEachTest(Method method) {
        ContainerConfig methodContainerConfig = classContainerConfig.clone();

        handleMethodEnvs(method, methodContainerConfig);

        dockerClient = new DockerCommandsHandler(methodContainerConfig);
        dockerClient.createAndStartContainer(methodContainerConfig);
    }

    @AfterMethod
    void AfterEachMethod(Method method) {
        dockerClient.stopContainer();

//        if (!method.isAnnotationPresent(KeepContainer.class)) {
//            dockerClient.removeContainer();
//        }
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
