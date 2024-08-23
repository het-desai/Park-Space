<?php 
	define('HOST','localhost');
	define('USER','root');
	define('PASS','dbroot');
	define('DB','park_space_db');
	
	if($_SERVER['REQUEST_METHOD'] == 'POST') {
		
		$conn = new mysqli(HOST, USER, PASS, DB);
		if ($conn->connect_error) {
			die(json_encode(array("Registration status" => "Unable to Connect")));
		}
		
		$name = $_POST['name'];
		$emailid = $_POST['email_id'];
		$registerno = $_POST['register_no'];
		$password = $_POST['password'];
		$mode = $_POST['mode'];
		
		if($name == '' || $password == '' || $emailid == '' || $registerno == '' || $mode == '') {
			echo json_encode(array("Registration status" => "Please fill all values"));
		} else {
			
			if($registerno == '*VISITOR*') {
				
				$stmt = $conn->prepare("SELECT * FROM user_information WHERE email_id=?");
				$stmt->bind_param("s", $emailid);
				$stmt->execute();
				$result = $stmt->get_result();
				
				if($result->num_rows > 0) {
					echo json_encode(array("Registration status" => "Registerno or Email already exist"));
				} else {
					
					$stmt = $conn->prepare("SELECT register_no FROM user_information WHERE mode=?");
					$stmt->bind_param("s", $mode);
					$stmt->execute();
					$result = $stmt->get_result();
					
					$larger_count = 0;
					
					if($result->num_rows > 0) {
						
						while($row = $result->fetch_row()) {
							$visitor_current_count = (int)$row[0];
							if($larger_count < $visitor_current_count) {
								$larger_count = $visitor_current_count;
							}
						}
						
						$registerno = (string)($larger_count + 1);
						
						$stmt = $conn->prepare("INSERT INTO user_information (name,email_id,register_no,password,mode) VALUES(?, ?, ?, ?, ?)");
						$stmt->bind_param("sssss", $name, $emailid, $registerno, $password, $mode);
						
						if($stmt->execute()) {
							echo json_encode(array("Registration status" => "Successfully registered"));
						} else {
							echo json_encode(array("Registration status" => "Oops! Please try again!"));
						}
						
					} else {
						
						$registerno = (string)($larger_count + 1);
						
						$stmt = $conn->prepare("INSERT INTO user_information (name,email_id,register_no,password,mode) VALUES(?, ?, ?, ?, ?)");
						$stmt->bind_param("sssss", $name, $emailid, $registerno, $password, $mode);
						
						if($stmt->execute()) {
							echo json_encode(array("Registration status" => "Successfully registered"));
						} else {
							echo json_encode(array("Registration status" => "Oops! Please try again!"));
						}
					}
				}
				
			} else {
				
				$stmt = $conn->prepare("SELECT register_no, email_id FROM user_information WHERE register_no=? OR email_id=?");
				$stmt->bind_param("ss", $registerno, $emailid);
				$stmt->execute();
				$result = $stmt->get_result();
				
				if($result->num_rows > 0) {
					echo json_encode(array("Registration status" => "Registerno or Email already exist"));
				} else {
					
					$stmt = $conn->prepare("INSERT INTO user_information (name,email_id,register_no,password,mode) VALUES(?, ?, ?, ?, ?)");
					$stmt->bind_param("sssss", $name, $emailid, $registerno, $password, $mode);
					
					if($stmt->execute()) {
						echo json_encode(array("Registration status" => "Successfully registered"));
					} else {
						echo json_encode(array("Registration status" => "Oops! Please try again!"));
					}
				}
			}
		}
		$conn->close();
	} else {
		echo json_encode(array("Registration status" => "Nice try, you're one step towards hacking"));
	}
?>