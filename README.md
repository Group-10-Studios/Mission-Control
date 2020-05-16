[![pipeline status](https://gitlab.ecs.vuw.ac.nz/course-work/engr300/2020/group10/group-10/badges/master/pipeline.svg)](https://gitlab.ecs.vuw.ac.nz/course-work/engr300/2020/group10/group-10/-/commits/master)

# Group 10 Mission Control

This software can be used as mission control for rocket launches based on the hardware developed by teams 1-6.

## Usage

### Prerequisites
The computer this code will run on reuqires the following dependencies:
- [Java 11](https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=hotspot)
- [JavaFX SDK](https://gluonhq.com/products/javafx/) > This is only needed if you are using a pre-built package on a different OS (Mac or BSD)

### Running the application
To run this application you can download the latest `.jar` from our pipeline.

Click on the link for your machines operating system:
- [Linux](https://gitlab.ecs.vuw.ac.nz/course-work/engr300/2020/group10/group-10/-/jobs/artifacts/master/download?job=package)
- [Windows](https://gitlab.ecs.vuw.ac.nz/course-work/engr300/2020/group10/group-10/-/jobs/artifacts/master/download?job=windows_package)

This will download a file called `artifacts.zip` within this there is a folder called `target` which contains the JAR file to run the software.

Then you can run:
```sh
java -jar mission-control.jar
```

If you have all dependencies installed this should launch without issues. However if you are missing JavaFX sdk it will result in a pop-up notifying you of the missing dependency.