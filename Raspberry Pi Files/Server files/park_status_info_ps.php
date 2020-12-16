<?php 
	define('HOST','localhost');

	define('USER','root');

	define('PASS','dbroot');

	define('DB','park_space_db');
	
	if($_SERVER['REQUEST_METHOD'] == 'POST') {
		
		$parkingno = $_GET['parking_no'];
		
		$con = mysqli_connect(HOST,USER,PASS,DB) or die(json_encode(array("Park status information" => "Unable to Connect")));
		
		$sql = "SELECT parking_spot_no, park_status FROM park_information WHERE parking_no='$parkingno'";
		
		$check = mysqli_query($con, $sql);
		
		$parking_spot_nos [] = array();
		$parking_spot_nos_status[] = array();
		$dbdata = array();
		
		$position = 0;
		
		$temp_parking_status = null;
		
		$dbdata = array();
		
		while($row = mysqli_fetch_row($check)) {
			
			$parking_spot_nos[$position] = (string)$row[0];
			$parking_spot_nos_status[$position] = (string)$row[1];
			
			if($parking_spot_nos_status[$position] == "Free") {
				
				if($parking_spot_nos[$position] == "3") {
					$parking_spot_nos[$position] = "1";
				} else if($parking_spot_nos[$position] == "4") {
					$parking_spot_nos[$position] = "2";
				}
				
				$sql = "SELECT parking_no, parking_spot_no FROM booking_information WHERE parking_no = '$parkingno' AND parking_spot_no = '$parking_spot_nos[$position]'";
				$temp_parking_status = mysqli_query($con, $sql);
				
				if($temp_parking_status -> num_rows > 0) {
					$parking_spot_nos_status[$position] = "Booked";
					$temp_parking_status = null;
				}
			}
			
			array_push($dbdata, $parking_spot_nos_status[$position]);
			
			$position = $position + 1;
			
		}
		
		echo json_encode($dbdata, JSON_PRETTY_PRINT);
	} else {
		echo json_encode(array("Park status information" => "Nice try, your one step towards the hacking"));
	}
?>
