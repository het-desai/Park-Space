import RPi.GPIO as GPIO1
import RPi.GPIO as GPIO2
import RPi.GPIO as GPIO3
import RPi.GPIO as GPIO4
import RPi.GPIO as GPIO5

import time, cv2

import pytesseract
import numpy as np
import cv2 as cv
from PIL import Image

import MySQLdb
from mysql_db_config import update_status_data_db
from mysql_db_config import vehicle_information_data_db
from mysql_db_config import vehicle_information_status_data_db
from mysql_db_config import booking_information_deleteable_db

GPIO1.setwarnings(False)
GPIO1.setmode(GPIO1.BOARD)
GPIO1.setup(16, GPIO1.IN, pull_up_down=GPIO1.PUD_DOWN)
sensor_1_current = GPIO1.input(16)
sensor_1_previous = sensor_1_current

GPIO2.setwarnings(False)
GPIO2.setmode(GPIO2.BOARD)
GPIO2.setup(18, GPIO2.IN, pull_up_down=GPIO2.PUD_DOWN)
sensor_2_current = GPIO2.input(18)
sensor_2_previous = sensor_2_current

GPIO3.setwarnings(False)
GPIO3.setmode(GPIO3.BOARD)
GPIO3.setup(13, GPIO3.IN, pull_up_down=GPIO3.PUD_DOWN)
sensor_3_current = GPIO3.input(13)
sensor_3_previous = sensor_3_current

GPIO4.setwarnings(False)
GPIO4.setmode(GPIO4.BOARD)
GPIO4.setup(31, GPIO4.IN, pull_up_down=GPIO4.PUD_DOWN)
sensor_4_current = GPIO4.input(31)
sensor_4_previous = sensor_4_current

GPIO5.setwarnings(False)
GPIO5.setmode(GPIO5.BOARD)
GPIO5.setup(7, GPIO5.IN, pull_up_down=GPIO5.PUD_DOWN)
sensor_5_current = GPIO5.input(7)
sensor_5_previous = sensor_5_current

conn = None

test = 0

try:
	
	#DB Connection establish
	conn = MySQLdb.connect(host="localhost", database="park_space_db", user="root", password="dbroot")
	cursor = conn.cursor()
	
	while True:
		
		#Sensor 1
		if GPIO1.input(16) == 0:
			sensor_1_current = "Detected"
		else:
			sensor_1_current = "Not Detected"
		
		if(sensor_1_current != sensor_1_previous):
			query = update_status_data_db(1, sensor_1_current)
			cursor.execute(query)
			conn.commit()
			print("Sensor 1 updated")
		sensor_1_previous = sensor_1_current
		
		#Sensor 2
		if GPIO2.input(18) == 0:
			sensor_2_current = "Detected"
		else:
			sensor_2_current = "Not Detected"
		
		if(sensor_2_current != sensor_2_previous):
			query = update_status_data_db(2, sensor_2_current)
			cursor.execute(query)
			conn.commit()
			print("Sensor 2 updated")
		sensor_2_previous = sensor_2_current
		
		#Sensor 3
		if GPIO3.input(13) == 0:
			sensor_3_current = "Detected"
		else:
			sensor_3_current = "Not Detected"
		
		if(sensor_3_current != sensor_3_previous):
			query = update_status_data_db(3, sensor_3_current)
			cursor.execute(query)
			conn.commit()
			print("Sensor 3 updated")
		sensor_3_previous = sensor_3_current
		
		#Sensor 4
		if GPIO4.input(31) == 0:
			sensor_4_current = "Detected"
		else:
			sensor_4_current = "Not Detected"
		
		if(sensor_4_current != sensor_4_previous):
			query = update_status_data_db(4, sensor_4_current)
			cursor.execute(query)
			conn.commit()
			print("Sensor 4 updated")
		sensor_4_previous = sensor_4_current
		
		#Sensor 5
		if GPIO5.input(7) == 0:
			sensor_5_current = "Detected"
		else:
			sensor_5_current = "Not Detected"
		
		if(((sensor_5_current != sensor_5_previous) & (test == 1)) & (sensor_5_current == "Detected")):
			no_plate_visible = "no"
			
			while(no_plate_visible != "yes"):
				#get the data
				cap = cv2.VideoCapture(0)
				cap.isOpened()
				if cap.isOpened() == False:
					cap = cv2.VideoCapture(1)
					cap.isOpened()
				ret, img_frame = cap.read()
				cv2.imshow('output', img_frame)
				cv2.waitKey(0)
				time.sleep(1)
				cv2.imwrite("current_no_plate.png", img_frame)
				cap.release()
				cv2.destroyAllWindows()
				img = Image.open("current_no_plate.png")
				text = pytesseract.image_to_string(img)
				actual_no_plate = ""
				for ch in text:
					iz = ord(ch)
					if(iz != 32):
						actual_no_plate += ch
				print(actual_no_plate)
				no_plate_visible = input("Is No. Plate visible (yes/no) : ")
			
			#check the vehicle information
			query = vehicle_information_status_data_db(actual_no_plate)
			cursor.execute(query)
			no_plate_status = cursor.rowcount
			
			#update the vehicle information
			query = vehicle_information_data_db(actual_no_plate, no_plate_status)
			cursor.execute(query)
			conn.commit()
			
			#check is deletable or not
			query = booking_information_deleteable_db(actual_no_plate)
			cursor.execute(query)
			conn.commit()
			
			if cursor.rowcount >= 1:
				#update the booking information
				query = booking_information_data_db(actual_no_plate)
				cursor.execute(query)
				conn.commit()
			print("Sensor 5 Updated")
			
		elif(test == 0):
			test += 1
		sensor_5_previous = sensor_5_current
		
		time.sleep(3)

except KeyboardInterrupt:
	print("Interrupt occur")

finally:
	cap.release()
	cv2.destroyAllWindows()
	GPIO1.cleanup()
	GPIO2.cleanup()
	GPIO3.cleanup()
	GPIO4.cleanup()
	GPIO5.cleanup()
	conn.close()
	cursor.close()
