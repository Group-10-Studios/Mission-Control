#!/bin/bash
# This is a basic bash script adapted from the basestation code provided with data from Josh Benfell's team. This will
# continuously send data to the specified port every 0.1 seconds which can be read by the application through a virtual
# serial port configuration for testing purposes.
# Author: Nathan Duckett

### USAGE:
### run with bash stream_serial_data.sh <serial_address>
### Where serial address is equal to the address for your serial device e.g. /dev/ttyS2

# Define the dataset with 4 random data strings to choose from in the format:
# time_stamp, lat, long, gimbal_x [degrees], gimbal_y[degrees], IMU_accel_x, IMU_accel_y, IMU_accel_z, IMU_rot_x, IMU_rot_y, IMU_rot_z, internal_temp, height, battery_status, rocket_status
data=("192674,-41.335,174.705,1,6,1.00,-0.16,8.54,-0.07,-0.07,-0.00,25.84,0,6.74,ARMED" "192872,-41.335,174.705,4,3,3.60,-0.16,8.11,-0.03,-0.89,0.12,26.23,0,6.53,ARMED" "192987,-41.335,174.705,10,3,4.24,-0.15,7.87,-0.03,-0.67,0.07,23.43,10,6.31,FLIGHT" "193081,-41.335,174.705,8,6,1.59,-0.51,10.13,-0.21,0.32,-0.02,21.20,20,6.28,FLIGHT")

echo "Outputting data to $1"
# Infinite loop data
while [ true ]; do
  # Get random index in inclusive range
	index=$(shuf -i 0-3 -n 1)
	echo -e ${data[index]} > $1
	sleep 0.1
done
