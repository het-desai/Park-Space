<?php 
	define('HOST','localhost');
	define('USER','root');
	define('PASS','dbroot');
	define('DB','park_space_db');

	if($_SERVER['REQUEST_METHOD'] == 'POST') {

		$conn = new mysqli(HOST, USER, PASS, DB);
		if ($conn->connect_error) {
			die(json_encode(array("status" => "Unable to Connect")));
		}

		$parking_id = $_POST['parking_id'];
		$user_id = $_POST['user_id'];
		$booking_time = $_POST['booking_time'];

		if($parking_id == '' || $user_id == '' || $booking_time == '') {
			echo json_encode(array("status" => "Please fill all values"));
		} else {
			$stmt = $conn->prepare("INSERT INTO bookings (parking_id, user_id, booking_time) VALUES (?, ?, ?)");
			$stmt->bind_param("sss", $parking_id, $user_id, $booking_time);

			if ($stmt->execute()) {
				echo json_encode(array("status" => "Booking successful"));
			} else {
				echo json_encode(array("status" => "Error: " . $stmt->error));
			}

			$stmt->close();
		}

		$conn->close();
	} else {
		echo json_encode(array("status" => "Invalid request method"));
	}
?>