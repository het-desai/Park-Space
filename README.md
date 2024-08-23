# Smart Parking System

## Overview

This project is a **Smart Parking System** designed to streamline the process of booking and managing parking spaces. The system utilizes a Raspberry Pi as a web server, where PHP APIs are hosted for interaction with an Android application. Users can book parking spaces via the Android app, while the Raspberry Pi collects real-time data from sensors, updates the MySQL database, and manages parking availability.

The project is accessible over the internet using the **Remoteit** tool, which makes the Raspberry Pi publicly available.

## Features

- **User-Friendly Android App**: Users can easily book parking spaces using the Android application.
- **Real-time Sensor Data Collection**: Python scripts running on the Raspberry Pi collect real-time data from IR sensors to monitor the status of parking spots.
- **Automated Database Management**: The system automatically updates the MySQL database with the latest parking information based on sensor data.
- **Remote Access**: The Raspberry Pi web server is accessible over the internet using the Remoteit tool, allowing users to book parking spaces from anywhere.

## Project Structure

### Hardware Components
- **Raspberry Pi**: Serves as a web server hosting PHP APIs and runs Python scripts to interface with sensors.
- **IR Sensors**: Monitor parking spots, detecting whether they are occupied or available.
- **Android Device**: Provides a user interface for booking and managing parking spaces through a dedicated application.

### Software Components
- **Python Scripts**:
  - `Park_Sensing_Status.py`: Collects data from IR sensors and updates the MySQL database with the status of each parking spot.
  - `mysql_db_config.py`: Contains SQL query functions used by the Python scripts to interact with the MySQL database.
  
- **PHP API**:
  - `booking_information_ps.php`: Manages the booking of parking spaces in the database.
  - `login_ps.php`: Handles user authentication for the Android app.
  - `park_status_info_ps.php`: Provides information on the current availability of parking spots.
  - `register_ps.php`: Manages user registration for the Android app.

- **Android Application**:
  - XML files: Define the UI layout for booking parking spaces and managing user accounts.
  - Java files: Handle user interactions and communicate with the Raspberry Pi server via PHP APIs.

### Tools Used
- **Remoteit**: Enables remote access to the Raspberry Pi, making it accessible as a public web server.

## Installation

### Prerequisites

- Raspberry Pi with Raspbian OS
- PhpMySQL server
- PHP installed on the Raspberry Pi
- Remoteit tool installed on Raspberry Pi
- Android Studio for building and running the Android application

### Setup

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/yourusername/smart-parking-system.git
   ```

2. **Setting Up the Raspberry Pi**:
   - Install required packages:
     ```bash
     sudo apt-get update && sudo apt-get upgrade
     sudo apt-get install python3-pip mysql-server php phpmyadmin
     ```
   - Install Python dependencies:
     ```bash
     pip3 install MySQLdb pytesseract numpy cv2
     ```
   - Set up the MySQL database. Set the username `root` and password `dbroot`. Create tables as mentioned in Photos.
   - Install and configure the Remoteit tool to make the Raspberry Pi publicly accessible. Installation YouTube video by [SPARKLERSWeAreTheMakers](https://www.youtube.com/watch?v=_B8E1dE5kW4)

3. **Deploying the PHP API**:
   - Place the PHP files in the `/var/www/html/` directory on your Raspberry Pi.
   - Update the database connection settings in the PHP files as needed.

4. **Running the Python Scripts**:
   - Ensure the `mysql_db_config.py` is properly configured.
   - Run the `Park_Sensing_Status.py` script:
     ```bash
     python3 Park_Sensing_Status.py
     ```

5. **Setting Up the Android Application**:
   - Open the project in Android Studio.
   - Configure the base URL of the PHP API in the Java files (Park-Space/Android  Files and Application/ParkSpace/app/src/main/java/com/example/parkspace/URLHandler.java) to point to your Raspberry Pi's public URL provided by Remoteit.
   - Build and run the application on an Android device.

## Usage

- **Register and Log In**: Users can register and log in through the Android application.
- **Book Parking Spaces**: The application allows users to view available parking spots and make bookings.
- **Monitor Parking Status**: The Android app provides real-time updates on parking spot availability based on data collected by the Raspberry Pi.

## API Endpoints

- **`booking_information_ps.php`**: Manages parking space bookings.
- **`login_ps.php`**: Handles user login.
- **`park_status_info_ps.php`**: Provides parking spot availability information.
- **`register_ps.php`**: Manages user registration.

## Architecture

- **Raspberry Pi**: Acts as a web server, running Python scripts to interface with sensors and hosting PHP APIs for the Android app.
- **Android Application**: Interfaces with the Raspberry Pi server to manage and book parking spaces.

## Sensor Wiring Diagram

![Hardware setup](https://github.com/het-desai/Park-Space/blob/main/Photos/Hardware%20setup.jpg "Hardware setup")