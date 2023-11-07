# Park-Space

## Goal

Book for a parking spot and save time, money, fuel, and environment

---

## How its works?

If any user wants to book a parking spot, they should book it 2-3 minutes before so they can easily book the parking and save time.

Fill up the registration details and add vehicle details. Now select the location where you want to park your vehicle and then select a parking spot. After this process, the application sends the data to the Raspberry Pi and keeps user and vehicle details in the database.

Now the parking spot is waiting for the user, and when the user is approaching the parking lot to get at it, the camera will open and capture the number plate and verify whether this vehicle is already booked or not! If the vehicle has already been booked, then the user can directly park it, or else if the unknown vehicle comes and the space is available, then the vehicle can be parked.

---

## Technology, Hardware, Software and Libraries

Software: Android studio, Remote.it, PhpMyAdmin

Hardware: Raspberry Pi 3B, Android Phone, Smart Phone GPS, IR Sensors, and Web Camera

Python Library: Numpy, cv2, and Pytesseract

---

## How to Setup Project

**Raspberry Pi Setup:**

1. Connect 5 IR Sensor (IR Sen. 1, IR Sen. 2,...) and camera to the Raspberry Pi

![Hardware Setup](https://github.com/het-desai/Park-Space/blob/main/Photos/Hardware%20setup.jpg "Hardware setup")

2. Install

- Install PhpMyAdmin, create a database, and give it the name park_space_db. Inside that database, create four tables.

![PhpMyAdmin Database's tables](https://github.com/het-desai/Park-Space/blob/main/Photos/PhpMyAdmin%20DB.PNG "PhpMyAdmin Database's tables")

**Note: Please set Username = "root" & Password = "dbroot")**

3. Install remote.it and configure (set WebPort 8080 or 80). For more detailed configuration, follow this [YT: Access Raspberry Pi ports from anywhere in the world without port forwarding using Remote.it](https://www.youtube.com/watch?v=_B8E1dE5kW4)

4. Keep two Python programme files in the same folder: Park_Sensing_Status.py and mysql_db_config.py.

5. Keep four PHP programme files put in the same folder (Folder Path in Raspberry Pi: /var/www/). booking_information_ps.php, login_ps.php, park_status_info_ps.php, register_ps.php

6. Now run python Park_Sensing.py

**Android Studio Setup**

1. Install Android studio and open the project file

2. Open this file [URLHandler.java](https://github.com/hetdesaii/Park-Space/blob/main/Android%20%20Files%20and%20Application/ParkSpace/app/src/main/java/com/example/parkspace/URLHandler.java "URL") and change the URL in line 5, which you got from the Remote.it web site.

3. Now build the application and install app in your phone.
