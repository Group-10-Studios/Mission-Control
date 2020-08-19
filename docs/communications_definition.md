# Setting up Communications definition

Our application has been designed to be built around the graphs and information you require. This information can be defined within the file `src/main/resources/config/communications.json`. This JSON file contains all of the basis for expected communications between avionics and with simulation providers. These definitions are for supporting incoming data from these sources.

## Basic Outline

`communications.json` is built with a slightly modified default JSON structure. Each incoming definition is defined as a key with an object connected.

e.g.
```json
{
    "incoming-avionics": {

    },
    "incoming-simulation": {

    }
}
```

Each key defines an expected table with data. Our application will default to look for the names:
- `incoming-avionics`
- `incoming-simulation`

These names define the default mappings which the application will use.
This should later allow the user to choose any definition name within the application to change from the default value.

## Source definition
Within each source we can define up to two options:
- col_split (optional)
- headers (required)

### col_split (optional)
Col_split allows you to define a custom CSV delimiter which the user can use to split the incoming data by.

e.g. using a tab or comma separated CSV data. `"col_split": "\t"`

Currently the application defaults to using a comma as the delimiter.

### headers
Headers defines all of the data headers/columns expected to be coming in with each received payload from the communication channel.
Each column allows you to set the primitive data type which can be accessed within the program (if it is supported) otherwise a custom
type must be used to allow the values to be drawn to the graphs.

Each key represents the name/label of the graph while the value matches the data type and determines what the data will be used for within the application.

The order of the headers is determined by the order of the incoming data within the CSV payload.

#### Supported Custom Data Types
- **time**: This represents a timestamp object which is accessed when mapping the graphed values against time.
- **angle**: This represents a value to be graphed as an angle. This defaults that the value is of type double, and is measured in degrees.
- **line-accel**: This represents a value to be graphed on a line chart. This is set for acceleration values which are of type double, and measured in m/s^2.
- **line-vel**: This represents a value to be graphed on a line chart. This is set for velocity values which are of type double, and measured in m/s.
- **height**: This represents an altitude reading which will be graphed on a line chart. This is of type double and measured in m.

#### Supported Primitive Data Types
- **string**
- **double**
- **float**
- **int**
- **long**

Primitive types are only supported when using default keywords for specific functions within the application.

This includes:
- "lat"/"long" (Rocket positions)
- "battery_status"
- "rocket_status"
- "internal_temp"

## Examples:

This example is based off an avionics team example CSV format they have created for the base station:
```json
"incoming-avionics": {
    "headers": {
      "timestamp": "time",
      "lat": "double", 
      "long": "double", 
      "gimbal_x": "angle",
      "gimbal_y": "angle",
      "IMU_accel_x": "line-accel",
      "IMU_accel_y": "line-accel",
      "IMU_accel_z": "line-accel",
      "IMU_rot_x": "angle",
      "IMU_rot_y": "angle",
      "IMU_rot_z": "angle",
      "internal_temp": "double",
      "height": "height",
      "battery_status": "double",
      "rocket_status": "string"
    }
  }
```