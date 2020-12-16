def update_status_data_db(parking_no, status):
	parking_no = str(parking_no)
	status = str(status)
	if (status == "Detected"):
		status = "Parked"
	elif (status == "Not Detected"):
		status = "Free"
	return "UPDATE park_information SET park_status = '" + status + "' WHERE parking_spot_no = " + parking_no
	
def vehicle_information_data_db(no_plate, vehicle_info_status):
	if (vehicle_info_status != 0):
		return "UPDATE vehicle_information SET in_time = CURRENT_TIMESTAMP WHERE number_plate = '" + no_plate + "'"
	else:
		return "INSERT INTO vehicle_information (number_plate, in_time) VALUES ('" + no_plate + "', CURRENT_TIMESTAMP)"
		
def vehicle_information_status_data_db(no_plate):
	return "SELECT number_plate FROM vehicle_information WHERE number_plate = '" + no_plate + "'"

def booking_information_data_db(no_plate):
	return "DELETE FROM booking_information WHERE number_plate = '" + no_plate + "'"

def booking_information_deleteable_db(no_plate):
	return "SELECT number_plate FROM booking_information WHERE number_plate = '" + no_plate + "'"
