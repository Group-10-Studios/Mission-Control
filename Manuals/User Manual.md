# Mission Control User Manual
## Software for Rocket Avionics

### Setup
#### Getting familiar with mission control
Getting familiar with the following diagram will aid your use of the manual, as the terms there will be the same as the terms referencing the user interface shown below. 
![Mission Control Software](https://gitlab.ecs.vuw.ac.nz/course-work/engr300/2020/group10/group-10/-/raw/5779eb602f296895f4209bad4dba441e7599cd24/Manuals/assets/user_interface_overview.png)

#### Getting started
To get started with Mission Control, select a serial device to connect to. The program will analyse the conditions and display warnings on the warnings pane should there be any.

#### Running simulations
Before running the actual rocket, it may be a good idea to running a simulation to ensure safe launch. To run a simulation, click the ‘Run Simulation’ button in the bottom left of the window. Select an appropriate CSV file.

#### Launch configurations
Before launching the rocket, you can edit the Launch Configurations. The parameters will be used by various parts of the program or by the rocket itself.
![Launch Configurations](https://gitlab.ecs.vuw.ac.nz/course-work/engr300/2020/group10/group-10/-/raw/5779eb602f296895f4209bad4dba441e7599cd24/Manuals/assets/launch_config.png)

##### Maximum launch angle
The maximum angle the rocket will launch from. The program will display a warning if this angle is exceeded.

##### Maximum wind speed
The maximum wind speed that is safe to launch the rocket from. The program will display a warning if the detected wind speed is exceeded.

##### Latitude
The latitude of the launching point of the rocket.

##### Longitude
The longitude of the launching point of the rocket.

##### Maximum ground hit speed
The maximum speed the rocket is allowed to hit the ground at. The program will display a warning if this speed is exceeded.

##### Maximum angle of attack
The maximum angle of attack of the rocket. The program will display a warning if this angle is exceeded.

##### Maximum parachute deploy speed
The maximum speed of the rocket that triggers the rocket’s parachute. The program will display a warning if this speed is exceeded.

#### Launching the Rocket
To arm the rocket to prepare for launch, click the Arm button. The rocket will move into a state that’s ready for launch. Warnings may appear in the warnings pane depending on the conditions. The Launch button will be activated and pressing so will initiate a countdown. If there are warnings, a dialogue box will appear asking for confirmation. The rocket will launch automatically after the countdown.

### During Launch
#### Interacting with real-time data
During the rocket’s flight, Mission Control will display flight conditions, such as altitude and velocity. The graphs will be displayed in real time, and hovering your mouse over the graph will show the value at that point in time. 

#### Customise viewing of data
You can reorder the graphs by clicking the arrows next to the corresponding graph button. You can also make a graph visible or hidden by clicking the corresponding V/H. Clicking on the button will highlight the graph. 

#### Battery
The two batteries connected to the rocket will be displayed in the top right of Mission Control. The battery icon on the left is the main battery, and the other one is a backup. Only when the main battery reaches 0% will the second battery begin to drain.

#### Warnings
Warnings may appear on the warnings pane during the rocket’s flight. The severity of each warning is described below:
* Blue outline: general notification.
* Yellow outline: warning the user should take action on.
* Red outline: immediate action should be taken by the user.

### Post Launch
_Work in progress, will be updated when we know what UI will display when a launch is complete_