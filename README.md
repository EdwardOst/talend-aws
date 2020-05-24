# Talend AWS

Helper classes for using AWS Services with Talend.

The helper classes can in turn be used by Talend Routines.  This allows dependencies to be centrally managed.  Dependency management is done here in the maven project for testing the helper class, and also in the Talend Routine to encapsulate dependencies from Jobs that use the Routine.  

Many AWS API are simple enough to require little composition of AWS SDK invocations, but if necessary it can be encapsulated in the helper.  Talend Routines should be simple wrappers.

In general, the helper pom should try to use versions consistent with Talend versions of libraries.  On the other hand, some AWS SDK's are only available in more recent AWS libraries, which may not yet be supported by AWS libraries included in Talend Studio.

In these cases the preferred path is to test as many Talend compliant library versions with the necessary newer AWS libraries here in the helper class to confirm they are compatible.

The strategy is to keep the Talend Routine as simple as possible so that use of Talend Studio is minimized and focused on composition of Jobs rather than low level depenency management.  The Eclipse environment is much better suited for the latter, and the build / test / iterate cycle will be much faster in Eclipse.

Maven dependencies can be enumerated with the maven _dependency:list_ command.  Any dependencies which are not included in the Talend Studio should be identified and called out in this documentation since the user will need to load those into Studio using either the `Edit Routine Libraries` dialog or the `Modules` dialog.

* [AWS Secrets Manager](aws-secrets-manager/README.md)
