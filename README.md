[![Build Status](https://travis-ci.org/shafr/docker-unit-test-framework.svg?branch=master)](https://travis-ci.org/shafr/docker-unit-test-framework)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/shafr/Docker-Unit-Test-Framework/blob/master/LICENSE.md)
[![Analytics](https://ga-beacon.appspot.com/UA-121934843-1/Docker-Unit-Test-Framework)](https://github.com/shafr/Docker-Unit-Test-Framework)
[![Codacy Quality](https://api.codacy.com/project/badge/Grade/5ab3407c0ae149fda98c1fc360fe7d7b)](https://www.codacy.com/app/shafr/Docker-Unit-Test-Framework?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=shafr/Docker-Unit-Test-Framework&amp;utm_campaign=Badge_Grade)
[![Codacy Coverage](https://api.codacy.com/project/badge/Coverage/5ab3407c0ae149fda98c1fc360fe7d7b)](https://www.codacy.com/app/shafr/Docker-Unit-Test-Framework?utm_source=github.com&utm_medium=referral&utm_content=shafr/Docker-Unit-Test-Framework&utm_campaign=Badge_Coverage)
[ ![Download](https://api.bintray.com/packages/shafr/io.github.shafr/docker-unit-test-framework/images/download.svg?version=0.1.5) ](https://bintray.com/shafr/io.github.shafr/docker-unit-test-framework/0.1.5/link)

__Note that at this point the project is in Proof-of-concept state, so everything can/would be changed.__

# Docker images testing with Java + TestNg.
__TLDR: This is merely wrapper around [Docker java plugin][docker-java] & TestNg Basic attributes.__

I was looking for any solution on how to test my Docker images (mostly for application servers), but I was not able to find one.

The Idea behind this tool - is to give you opportunity to test Docker images `as a Unit tests`, where you can specify CommandLine Arguments, Env. properties, etc per-test.

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
| ~~Port~~            | X      | X     |           | TBD                                                      |

## Compatibility
See [Docker-java][docker-java] compatibility list. Personally for me works on both Windows10 + LinunxCe and Windows10 + Remote CentOs machine.

## Helper methods:
| Method                                          | Description                             |
| ----------------------------------------------- | --------------------------------------- |
| String getLog(int timeoutInMs)                  | returns log before timeout occurs       |
| boolean waitForContainerToExit(int timeoutInMs) | retuns true if container is not running |
| int getExitCode(int timeoutInMs)                | returns exit code                       |
| String executeInsideContainer(String... cmds    | returns logs from execution             |

## How to Use / Examples of attributes:
See [Tests][test-cases-link] or for examples.

## Hints
* `dockerClient` field is available to you for accessing different container/host info from tests. 

* Remember that testng's `Test` annotation has some useful annotations - for example @Timeout or InvocationTimeout.

* If you are working on Windows - remember that you can delegate images handling to any Docker-compatible Linux machine (By setting `DockerHost` env property ).

[java-api-wiki]: https://github.com/spotify/docker-client/blob/master/docs/user_manual.md
[docker-java]: https://github.com/spotify/docker-client
[test-cases-link]: https://github.com/shafr/docker-unit-test-framework/tree/master/src/test/java/io/github/shafr