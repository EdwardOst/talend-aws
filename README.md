# Talend AWS

Helper classes for using AWS Services with Talend.

The helper classes can in turn be used by Talend Routines.  This allows dependencies to be centrally managed.  Dependency management is done here in the maven project for testing the helper class, and also in the Talend Routine to encapsulate dependencies from Jobs that use the Routine.  

Many AWS API are simple enough to require little composition of AWS SDK invocations, but if necessary it can be encapsulated in the helper.

Talend Routines should be simple wrappers.  The strategy is to keep the Talend Routine as simple as possible so that use of Talend Studio is minimized and focused on composition of Jobs rather than low level depenency management.

The Eclipse environment is much better suited for the detailed dependency management, and the build / test / iterate cycle will be much faster in Eclipse.

## Dependency Scope and Exclusions

Keep in mind that most dependencies can be left as a test scope dependencies unless they are actually used directly by the Helper class.  

In some cases aws sdk may have dependencies on commons-logging.  In these cases an exclusion should be used to remove the commons-logging dependency and slf4j-api and jcl-over-slf4j dependencies should be added.

Actual logging implementation and slf4j bindings to those implementations can be marked as test scope dependencies, but it should be the responsibility of the Talend Routine to specify actual realization.

## Dependency Version Management

In general, the helper pom should try to use versions consistent with Talend Studio versions of libraries.  In some cases it may be that Talend Studio only supports newer versions of libraries or transitive dependencies.

On the other hand, some AWS SDK's are only available in more recent AWS libraries, which may not yet be supported by AWS libraries included in Talend Studio.  This may in turn introduce new transitive dependencies which are also not in Talend Studio.

In both cases it should be ok to use newer versions as long as semantic versioning is used and they only differ by the minor or patch numbers.

In some cases it may be that Talend Studio supports multiple versions of a library.  In this case try using the latest compatible version in Studio in order to maximize the consistency with the same library introduced by other components in a Talend job.

Any dependencies which are not included in the Talend Studio should be identified and called out in the documentation since the user will need to load those into Studio using either the `Edit Routine Libraries` dialog or the `Modules` dialog.

Maven dependencies can be enumerated with the maven _dependency:list_ command.  

    mvn dependency:list

To determine why a specific version of a dependency is being used, try the maven tree command.

````
    # show dependency resolution for all jars
    mvn dependency:tree
[INFO] com.talend.se.aws:secretsmanager:jar:0.0.1-SNAPSHOT
[INFO] +- com.amazonaws:aws-java-sdk-secretsmanager:jar:1.11.779:compile
[INFO] |  +- com.amazonaws:aws-java-sdk-core:jar:1.11.779:compile
[INFO] |  |  +- org.apache.httpcomponents:httpclient:jar:4.5.9:compile
[INFO] |  |  |  +- org.apache.httpcomponents:httpcore:jar:4.4.11:compile
[INFO] |  |  |  \- commons-codec:commons-codec:jar:1.11:compile
[INFO] |  |  +- software.amazon.ion:ion-java:jar:1.0.2:compile
[INFO] |  |  +- com.fasterxml.jackson.core:jackson-databind:jar:2.6.7.3:compile
[INFO] |  |  |  +- com.fasterxml.jackson.core:jackson-annotations:jar:2.6.0:compile
[INFO] |  |  |  \- com.fasterxml.jackson.core:jackson-core:jar:2.6.7:compile
[INFO] |  |  +- com.fasterxml.jackson.dataformat:jackson-dataformat-cbor:jar:2.6.7:compile
[INFO] |  |  \- joda-time:joda-time:jar:2.8.1:compile
[INFO] |  \- com.amazonaws:jmespath-java:jar:1.11.779:compile
[INFO] +- org.slf4j:slf4j-api:jar:1.7.25:compile
[INFO] +- org.slf4j:jcl-over-slf4j:jar:1.7.25:compile
[INFO] +- org.apache.logging.log4j:log4j-slf4j-impl:jar:2.12.1:test
[INFO] +- org.apache.logging.log4j:log4j-api:jar:2.12.1:test
[INFO] +- org.apache.logging.log4j:log4j-core:jar:2.12.1:test
[INFO] \- junit:junit:jar:4.8.1:test


    # show dependency resolution for a single jar
    mvn dependency:tree -Dverbose -Dincludes=com.fasterxml.jackson.core:jackson-core
[INFO] com.talend.se.aws:secretsmanager:jar:0.0.1-SNAPSHOT
[INFO] \- com.amazonaws:aws-java-sdk-secretsmanager:jar:1.11.779:compile
[INFO]    \- com.amazonaws:aws-java-sdk-core:jar:1.11.779:compile
[INFO]       +- com.fasterxml.jackson.core:jackson-databind:jar:2.6.7.3:compile
[INFO]       |  \- com.fasterxml.jackson.core:jackson-core:jar:2.6.7:compile
[INFO]       \- com.fasterxml.jackson.dataformat:jackson-dataformat-cbor:jar:2.6.7:compile
[INFO]          \- (com.fasterxml.jackson.core:jackson-core:jar:2.6.7:compile - omitted for duplicate)
````

Note that the verbose option shows why other versions were not selected and is quite valuable.  But it is not supported in maven-dependency-plugin 3.x, although there is a pending feature request.  Workaround is to use an older version of the plugin.

````
    mvn org.apache.maven.plugins:maven-dependency-plugin:2.10:tree -Dverbose=true
[INFO] com.talend.se.aws:secretsmanager:jar:0.0.1-SNAPSHOT
[INFO] +- com.amazonaws:aws-java-sdk-secretsmanager:jar:1.11.779:compile
[INFO] |  +- com.amazonaws:aws-java-sdk-core:jar:1.11.779:compile
[INFO] |  |  +- org.apache.httpcomponents:httpclient:jar:4.5.9:compile
[INFO] |  |  |  +- org.apache.httpcomponents:httpcore:jar:4.4.11:compile
[INFO] |  |  |  \- commons-codec:commons-codec:jar:1.11:compile
[INFO] |  |  +- software.amazon.ion:ion-java:jar:1.0.2:compile
[INFO] |  |  +- com.fasterxml.jackson.core:jackson-databind:jar:2.6.7.3:compile
[INFO] |  |  |  +- com.fasterxml.jackson.core:jackson-annotations:jar:2.6.0:compile
[INFO] |  |  |  \- com.fasterxml.jackson.core:jackson-core:jar:2.6.7:compile
[INFO] |  |  +- com.fasterxml.jackson.dataformat:jackson-dataformat-cbor:jar:2.6.7:compile
[INFO] |  |  |  \- (com.fasterxml.jackson.core:jackson-core:jar:2.6.7:compile - omitted for duplicate)
[INFO] |  |  \- joda-time:joda-time:jar:2.8.1:compile
[INFO] |  \- com.amazonaws:jmespath-java:jar:1.11.779:compile
[INFO] |     \- (com.fasterxml.jackson.core:jackson-databind:jar:2.6.7.3:compile - omitted for duplicate)
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

You can also use the `Dependency Hierarchy` tab of the pom in Eclipse.  Clicking on a jar in the right hand `Resolved Dependencies` panel will show the resolution path in the left hand `Dependency Hierarchy` panel.

## Modules

* [AWS Secrets Manager](aws-secrets-manager/README.md)
