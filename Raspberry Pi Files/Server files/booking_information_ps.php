<?php 
	define('HOST','localhost');

	define('USER','root');

	define('PASS','dbroot');

	define('DB','park_space_db');
	
	
	if($_SERVER['REQUEST_METHOD'] == 'POST') {
		
		$con = mysqli_connect(HOST,USER,PASS,DB) or die(json_encode(array("Booking status" => "Unable to Connect")));
		
		$emailid = $_GET['email_id'];
		
		$noplate = $_GET['number_plate'];
		
		$parkingno = $_GET['parking_no'];
		
		$parkingspotno = $_GET['parking_spot_no'];
		
		if($emailid == '' || $noplate == '' || $parkingno == '' || $parkingspotno == '') {
			echo json_encode(array("Booking status" => "Please fill all values"));
		}
		else {
		
			$sql = "SELECT * FROM booking_information WHERE email_id='$emailid' OR number_plate='$noplate'";
		
			$check = mysqli_fetch_array(mysqli_query($con,$sql));
		
			if(isset($check)) {
				echo json_encode(array("Booking status" => "Vehicle or User already parked"));
			} else {
			
				$sql = "INSERT INTO booking_information (email_id,number_plate,parking_no,parking_spot_no) VALUES('$emailid','$noplate','$parkingno','$parkingspotno')";
			
				if(mysqli_query($con,$sql)) {
					echo json_encode(array("Booking status" => "Slot already booked"));
				} else {
					echo json_encode(array("Booking status" => "Oops! Please try again!"));;
				}
			}
		}
	} else {
		echo json_encode(array("Booking status" => "Nice try, your one step towards the hacking"));
	}
?>
