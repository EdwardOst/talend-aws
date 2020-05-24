# Secrets Manager

Simple helper class to be used by Talend Routines using the AWS Secrets Manager API.


## Dependencies

All compile scope dependencies except for those prefixed with a hyphen (-) need to be referenced by the helper Routine in Studio via `Edit Routine Dependencies`.  Dependencies prefixed with a hyphen are automatically included in all Talend jobs.  Test scope dependencies do not need to be added.

Bolded libraries will need to be added to Studio library modules in order to be able to be referenced.

Italicized libraries must have their versions specified in the helper pom so that they override aws default dependencies.

| Library                         |Studio Version  | AWS Version | Helper Version    | Helper Scope |
|:-------------------------------:|:--------------:|:-----------:|:-----------------:|:------------:|
| -slf4j-api                      |  1.7.25        |  N/A        | 1.7.25            | compile      |
| -log4j-slf4j-impl               |  2.12.1        |  N/A        | 1.7.25            | compile      |
| -log4j-api                      |  2.12.1        |  N/A        | 2.12.1            | test         |
| -log4j-core                     |  2.12.1        |  N/A        | 2.12.1            | test         |
| jcl-over-slf4j                  |  1.7.25        |  N/A        | 1.7.25            | compile      |
| **aws-java-sdk-secretsmanager** |  N/A           | 1.11.779    | 1.11.779          | compile      |
| **aws-java-sdk-core**           |  1.11.269      | 1.11.779    | 1.11.779          | compile      |
| **httpclient**                  |  4.5.6         | 4.5.9       | 4.5.9             | compile      |
| **httpcore**                    |  4.4.10        | 4.4.11      | 4.4.11            | compile      |
| **jmespath-java**               |  N/A           | 1.11.779    | 1.11.779          | compile      |
| jackson-databind                |  2.10.1        | 2.6.7.3     | _2.10.1_          | compile      |
| jackson-annotations             |  2.10.1        | 2.6.0       | _2.10.1_          | compile      |
| jackson-core                    |  2.10.1        | 2.6.7       | _2.10.1_          | compile      |
| jackson-dataformat-cbor         |  2.10.1        | 2.6.7       | _2.10.1_          | compile      |
| joda-time                       |  2.9.9         | 2.8.1       | _2.9.9_           | compile      |
| ion-java                        |  1.0.2         | 1.0.2       | 1.0.2             | compile      |


## Logging

The `aws-java-sdk-secretsmanager` has a dependency on `commons-logging`.  Since Talend does not use commons-logging, an exclusion is used.  The helper package adds the `jcl-over-slf4j` so that calls to commons-logging are routed to slf4j.  Version 1.7.25 of slf4j and jcl-over-slf4j is available in Studio so it is used here.

The test case uses the `log4j-slf4j-impl` binding together with log4j api and core.  These versions are not constrained since they are only test scope, but use the log4j 2.12.1 which is used by Talend Stuio.

## Dependency Management

Dependencies for this maven project can be enumerated with the maven _dependency:list_ command.

````
mvn dependency:list

[INFO]    org.slf4j:jcl-over-slf4j:jar:1.7.25:compile
[INFO]    org.apache.httpcomponents:httpclient:jar:4.5.9:compile
[INFO]    joda-time:joda-time:jar:2.9.9:compile
[INFO]    org.apache.httpcomponents:httpcore:jar:4.4.11:compile
[INFO]    com.amazonaws:jmespath-java:jar:1.11.779:compile
[INFO]    com.fasterxml.jackson.core:jackson-databind:jar:2.10.1:compile
[INFO]    org.apache.logging.log4j:log4j-core:jar:2.12.1:test
[INFO]    com.amazonaws:aws-java-sdk-core:jar:1.11.779:compile
[INFO]    org.slf4j:slf4j-api:jar:1.7.25:compile
[INFO]    com.amazonaws:aws-java-sdk-secretsmanager:jar:1.11.779:compile
[INFO]    com.fasterxml.jackson.core:jackson-core:jar:2.10.1:compile
[INFO]    org.apache.logging.log4j:log4j-api:jar:2.12.1:test
[INFO]    com.fasterxml.jackson.core:jackson-annotations:jar:2.10.1:compile
[INFO]    junit:junit:jar:4.8.1:test
[INFO]    software.amazon.ion:ion-java:jar:1.0.2:compile
[INFO]    com.fasterxml.jackson.dataformat:jackson-dataformat-cbor:jar:2.10.1:compile
[INFO]    commons-codec:commons-codec:jar:1.11:compile
[INFO]    org.apache.logging.log4j:log4j-slf4j-impl:jar:2.12.1:test
````

Use the maven tree command to show how dependencies were resolved.

````
mvn org.apache.maven.plugins:maven-dependency-plugin:2.10:tree -Dverbose=true

[INFO] com.talend.se.aws:secretsmanager:jar:0.0.1-SNAPSHOT
[INFO] +- com.amazonaws:aws-java-sdk-secretsmanager:jar:1.11.779:compile
[INFO] |  +- com.amazonaws:aws-java-sdk-core:jar:1.11.779:compile
[INFO] |  |  +- org.apache.httpcomponents:httpclient:jar:4.5.9:compile
[INFO] |  |  |  +- org.apache.httpcomponents:httpcore:jar:4.4.11:compile
[INFO] |  |  |  \- commons-codec:commons-codec:jar:1.11:compile
[INFO] |  |  +- software.amazon.ion:ion-java:jar:1.0.2:compile
[INFO] |  |  +- (com.fasterxml.jackson.core:jackson-databind:jar:2.6.7.3:compile - omitted for conflict with 2.10.1)
[INFO] |  |  +- (com.fasterxml.jackson.dataformat:jackson-dataformat-cbor:jar:2.6.7:compile - omitted for conflict with 2.10.1)
[INFO] |  |  \- (joda-time:joda-time:jar:2.8.1:compile - omitted for conflict with 2.9.9)
[INFO] |  \- com.amazonaws:jmespath-java:jar:1.11.779:compile
[INFO] |     \- (com.fasterxml.jackson.core:jackson-databind:jar:2.6.7.3:compile - omitted for duplicate)
[INFO] +- com.fasterxml.jackson.core:jackson-databind:jar:2.10.1:compile
[INFO] |  +- com.fasterxml.jackson.core:jackson-annotations:jar:2.10.1:compile
[INFO] |  \- com.fasterxml.jackson.core:jackson-core:jar:2.10.1:compile
[INFO] +- com.fasterxml.jackson.dataformat:jackson-dataformat-cbor:jar:2.10.1:compile
[INFO] |  \- (com.fasterxml.jackson.core:jackson-core:jar:2.10.1:compile - omitted for duplicate)
[INFO] +- joda-time:joda-time:jar:2.9.9:compile
[INFO] +- org.slf4j:slf4j-api:jar:1.7.25:compile
[INFO] +- org.slf4j:jcl-over-slf4j:jar:1.7.25:compile
[INFO] |  \- (org.slf4j:slf4j-api:jar:1.7.25:compile - omitted for duplicate)
[INFO] +- org.apache.logging.log4j:log4j-slf4j-impl:jar:2.12.1:test
[INFO] |  +- (org.slf4j:slf4j-api:jar:1.7.25:test - omitted for duplicate)
[INFO] |  +- (org.apache.logging.log4j:log4j-api:jar:2.12.1:test - omitted for duplicate)
[INFO] |  \- (org.apache.logging.log4j:log4j-core:jar:2.12.1:test - omitted for duplicate)
[INFO] +- org.apache.logging.log4j:log4j-api:jar:2.12.1:test
[INFO] +- org.apache.logging.log4j:log4j-core:jar:2.12.1:test
[INFO] |  \- (org.apache.logging.log4j:log4j-api:jar:2.12.1:test - omitted for duplicate)
[INFO] \- junit:junit:jar:4.8.1:test
````
