[![Build Status](https://travis-ci.org/shafr/Docker-Unit-Test-Framework.svg?branch=master)](https://travis-ci.org/shafr/Docker-Unit-Test-Framework)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/shafr/Docker-Unit-Test-Framework/blob/master/LICENSE.md)
[![Analytics](https://ga-beacon.appspot.com/UA-121934843-1/Docker-Unit-Test-Framework)](https://github.com/shafr/Docker-Unit-Test-Framework)
[![Codacy Quality](https://api.codacy.com/project/badge/Grade/5ab3407c0ae149fda98c1fc360fe7d7b)](https://www.codacy.com/app/shafr/Docker-Unit-Test-Framework?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=shafr/Docker-Unit-Test-Framework&amp;utm_campaign=Badge_Grade)
[![Codacy Coverage](https://api.codacy.com/project/badge/Coverage/5ab3407c0ae149fda98c1fc360fe7d7b)](https://www.codacy.com/app/shafr/Docker-Unit-Test-Framework?utm_source=github.com&utm_medium=referral&utm_content=shafr/Docker-Unit-Test-Framework&utm_campaign=Badge_Coverage)

__Note that at this point the project is in Proof-of-concept state, so everything can/would be changed.__

# Docker images testing with Java + TestNg.

The Idea behind is that you can use Annotations on Class or Methods to set different Docker parameters - Environment properties, configure Volumes, etc.

This would allow testing behaviour of Docker images with different ENV properties, 

startup arguments, or checking contents of docker image by executing different container commands.

`TLDR: This is merely wrapper around [Docker java plugin][docker-java] & TestNg Basic attributes.`


## How to Use / Examples of attributes:
See [Annotation tests][test-cases-link] for examples.

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
| ~~Volume~~          | X      | X     |           | TBD                                                      |
| ~~Port  ~~          | X      | X     |           | TBD                                                      |

## Helper methods:
| Method                                          | Description                             |
| ----------------------------------------------- | --------------------------------------- |
| String getLog(int timeoutInMs)                  | returns log before timeout occurs       |
| boolean waitForContainerToExit(int timeoutInMs) | retuns true if container is not running |
| int getExitCode(int timeoutInMs)                | returns exit code                       |


[java-api-wiki]: https://github.com/docker-java/docker-java/wiki
[docker-java]: https://github.com/docker-java/docker-java
[test-cases-link]: https://github.com/shafr/Docker-Unit-Test-Framework/tree/master/src/test/java/com/cyberneticscore/dockertestframework