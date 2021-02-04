## Park-Space

### Goal
    Book for a parking spot and save time, money, fuel, and environment
---
### How its works?

    Any user wants to book the parking spot then book that parking before 2-3 minutes before so they easily book the parking and save time.

    Fill up the registration detail and add vehicle details now select the location where do you want to park your vehicle and then select parking spot. After this process application sends the data to raspberry pi and keeps user and vehicle details in the database.

    Now parking spot is waiting for the user and when the user is approaching the parking to get at the camera will open and capture the number plate and verify this vehicle is already booked or not! if already book then the user can directly park a vehicle or else if the unknown vehicle comes if the space is available then the vehicle can be parked.

---
### Technology, Hardware, Software and Libraries

- Android, Raspberry Pi, GPS
- IR Sensors, Camera
- PhpMyAdmin, Android Studio, Remote.it
- Numpy, CV2, Pytesseract, API's

![Camera]() ![IR Sensor]() ![Raspberry Pi]() ![Android Phone]()

---

### How to Setup Project
  **Raspberry Pi Setup:**
  
   - Connect 5 IR Sensor and camera to Raspberry Pi
	 IR Sensor pin diagram mention below
   ![Circuit Diagram]()
    
   - Install
   
      1. PhpMyAdmin and make 1 database and give name park_space_db
		inside that database make 4 table
      
      ![Database and Table]()
      
      (Note : Please set Username = "root" & Password = "dbroot")
      
      2. remote.it and configure (set WebPort 8080 or 80)
      (Refered : Installation steps are mention very well on YouTube)
    
- Keep 2 python program file put in same folder Park_Sensing_Status.py, mysql_db_config.py

- Now run python Park_Sensing.py
	
 **Android Studio Setup**
 
- Install Android studio and open the project file

- Open this file [URLHandler.java](https://github.com/hetdesaii/Park-Space/blob/main/Android%20%20Files%20and%20Application/ParkSpace/app/src/main/java/com/example/parkspace/URLHandler.java "URL") and change the URL which you got from the Remote.it web site.

- Extra modification

   1. If you want to change parking spots then add, edit or remove location from this file [BookParking.java](https://github.com/hetdesaii/Park-Space/blob/main/Android%20%20Files%20and%20Application/ParkSpace/app/src/main/java/com/example/parkspace/BookParking.java "URL") for more details look the below screenshot.
  
     Example:
     ![BookParking.java]()
	
- Now build the application and install app in your phone.
