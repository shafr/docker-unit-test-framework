__Note that at this point the project is in Proof-of-concept state, so everything can/would be changed.__

# This is my attempt to make testing Docker images easier using Java + TestNg.

The Idea behind is that you can use Annotations on Class or Methods to set different Docker parameters - Environment properties, configure Volumes, etc.

This would allow testing behaviour of Docker images with different ENV properties, startup arguments, or checking contents of docker image by executing different container commands.

## How to Use / Examples of attributes:
```java
@DockerHost("someHost")
@EntryPoint("/bin/someentrypoint")
@Environment("key=value")
@Environment("key2=value2")
@Image("imageName")
@Volume(local = "local", remote = "remote")
@Volume(local = "local2", remote = "remote2")

public class ClassLevelTest extends DockerTest{

    @Test
    public void TestHost(){
        
    }
}

```

## Hints
`dockerClient` field is available to you for accessing different container/host info from tests.

## Helper methods:
This is todo at this point...