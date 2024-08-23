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

		$username = $_POST['username'];
		$password = $_POST['password'];

		if($username == '' || $password == '') {
			echo json_encode(array("status" => "Please fill all values"));
		} else {
			$stmt = $conn->prepare("SELECT * FROM users WHERE username=? AND password=?");
			$stmt->bind_param("ss", $username, $password);
			$stmt->execute();
			$result = $stmt->get_result();

			if ($result->num_rows > 0) {
				echo json_encode(array("status" => "Login successful"));
			} else {
				echo json_encode(array("status" => "Invalid username or password"));
			}

			$stmt->close();
		}

		$conn->close();
	} else {
		echo json_encode(array("status" => "Invalid request method"));
	}
?>