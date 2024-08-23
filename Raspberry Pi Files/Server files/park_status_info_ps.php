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

		if($parking_id == '') {
			echo json_encode(array("status" => "Please provide a parking ID"));
		} else {
			$stmt = $conn->prepare("SELECT status FROM parking_spots WHERE parking_id=?");
			$stmt->bind_param("s", $parking_id);
			$stmt->execute();
			$result = $stmt->get_result();

			if ($result->num_rows > 0) {
				$row = $result->fetch_assoc();
				echo json_encode(array("status" => "Parking spot found", "data" => $row['status']));
			} else {
				echo json_encode(array("status" => "No parking spot found with that ID"));
			}

			$stmt->close();
		}

		$conn->close();
	} else {
		echo json_encode(array("status" => "Invalid request method"));
	}
?>