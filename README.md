[![Build Status](https://travis-ci.org/shafr/Docker-Unit-Test-Framework.svg?branch=master)](https://travis-ci.org/shafr/Docker-Unit-Test-Framework)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/shafr/Docker-Unit-Test-Framework/blob/master/LICENSE.md)
[![Analytics](https://ga-beacon.appspot.com/UA-121934843-1/Docker-Unit-Test-Framework)](https://github.com/shafr/Docker-Unit-Test-Framework)
[![codecov](https://codecov.io/gh/shafr/Docker-Unit-Test-Framework/branch/master/graph/badge.svg)](https://codecov.io/gh/shafr/Docker-Unit-Test-Framework)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/5ab3407c0ae149fda98c1fc360fe7d7b)](https://www.codacy.com/app/shafr/Docker-Unit-Test-Framework?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=shafr/Docker-Unit-Test-Framework&amp;utm_campaign=Badge_Grade)

__Note that at this point the project is in Proof-of-concept state, so everything can/would be changed.__

# Docker images testing with Java + TestNg.

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
Remember that testng's `Test` annotation has some useful annotations - for example @Timeout or InvocationTimeout.

## Annotations
| Annotation          | Method | Class | Mandatory | Note                                                     |
| ------------------- | ------ | ----- | --------- | -------------------------------------------------------- |
| DockerHost          |        | X     | X         | Defaults to 'tcp://localhost:2375'                       |
| Image               |        | X     | X         | Mandatory                                                |
| Entrypoint          | X      | X     |           |                                                          |
| Environment         | X      | X     |           | One or more attributes allowded, format `"key=value"``   |
| CommandLineArgument | X      | X     |           | Array from command and arguments                         |
| KeepContainer       | X      |       |           | Does not remove container after test (for investigation) |
| CreateOnly          | X      |       |           | Does not start container, just create                    |
| Volume              | X      | X     |           | Exposes volume                                           |

## Helper methods:
This is todo at this point...