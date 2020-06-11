[![pipeline status](https://gitlab.ecs.vuw.ac.nz/course-work/engr300/2020/group10/group-10/badges/master/pipeline.svg)](https://gitlab.ecs.vuw.ac.nz/course-work/engr300/2020/group10/group-10/-/commits/master)
[![coverage report](https://gitlab.ecs.vuw.ac.nz/course-work/engr300/2020/group10/group-10/badges/master/coverage.svg)](https://gitlab.ecs.vuw.ac.nz/course-work/engr300/2020/group10/group-10/-/commits/master)

# Group 10 Mission Control

This software can be used as mission control for rocket launches based on the hardware developed by teams 1-6.

## Usage - Compile
After cloning our repo you can run the following commands using maven on our project:

- `mvn clean javafx:run` > This will run our mission control UI. This will allow you to run simulations, and use our software.
- `mvn clean test` > This will run our test suite on your system.
- `mvn clean package` > This will package our application into a fat JAR which can work on your machine. (Can be found in `target/`).

When testing the software using `mvn clean test` this will run our Unit tests and our GUI integration tests.
Currently we use [TestFX](https://github.com/TestFX/TestFX) as our testing framework which has a bug which
prevents our tests from passing on macOS machines. This is due to mouse permissions and being unable to correctly utilize the hardware.

The tests can pass on Windows and Linux operating systems - as shown in our CI pipeline.

### Prerequisites
Your machine needs the following software to compile, run and development.

- Java 11 JDK
- Maven

You will need a `keys.json` inside `src/main/resources/`. This file is a JSON file which contains the keys
required to access our API services, refer to API Keys to see the format.

### API Keys

Inside the file `src/main/resources/key.json` it requires the following information:

```json
{
    "weather": "OpenWeatherMap API key",
    "maps": "TomTom Maps Service API key"
}
```

### How to use:
Our application supports loading in simulation data generated from OpenRocket. These files are stored in `src/main/resources/test-data/`.

To use the application start using `mvn clean javafx:run` this will build and begin the java application.

**If the application positions look out of place resize the window slowly to dynamically update to fix the display.**

To start a simulation you must click on `Run Simulation` button in the bottom left corner. From there navigate to `test-data` and choose a CSV file. Once you open this file it will start a simulation and run the application as if it was running an actual launch displaying the data.


## Usage - JAR package
This is an executable JAR package which can run our mission-control software. This is expected to work with the rocket communication during release.

**NOTE: This does not allow you to run simulation files unless you have them manually downloaded - I would recommend using `Usage - Compile` instead**

### Prerequisites
The computer this code will run on requires the following dependencies:
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

If you have all dependencies installed this should launch without issues. However if you are missing JavaFX SDK it will result in a pop-up notifying you of the missing dependency.

## Project Information Documents

These are automatically generated from the latest version from the `master` branch in the Gitlab CI pipeline.

- [Test Coverage](https://course-work.glp.ecs.vuw.ac.nz/engr300/2020/group10/group-10/jacoco/)
- [JavaDocs](https://course-work.glp.ecs.vuw.ac.nz/engr300/2020/group10/group-10/javadoc/)

## Extra information

- Our current Epic's show 0% completion this is a [known issue](https://gitlab.com/gitlab-org/gitlab/-/issues/215091) with GitLab and GitLab EE. This will be patched on 22nd May according to the issue post. This will be delayed depending on how fast the University can deploy the update to the Gitlab instance. Until then it will show 0% completion on our Epic's which does not reflect our progress.
- We have been time tracking using GitLab issues and milestones. These can be viewed within `Issues>Milestones>Closed` or [here](https://gitlab.ecs.vuw.ac.nz/course-work/engr300/2020/group10/group-10/-/milestones?sort=due_date_desc&state=closed). This outlines our previous sprints where you can see completed tickets, time spent during the week and other information. These can also be viewed in `Issues>Boards` where you can then select the board for each sprint to view the previous information of completed work.
- We have a cron job updating the [wiki here](https://gitlab.ecs.vuw.ac.nz/course-work/engr300/2020/group10/group-10/-/wikis/Time-Tracking-by-Team-Member) which shows the time which team members have spent on tickets and in each group meeting based on each sprint for each week. This is an easier way to view our time spent than inside our milestones.