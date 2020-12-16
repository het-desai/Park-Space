<?php 
	define('HOST','localhost');

	define('USER','root');

	define('PASS','dbroot');

	define('DB','park_space_db');
	
	
	if($_SERVER['REQUEST_METHOD'] == 'POST') {
		
		$con = mysqli_connect(HOST,USER,PASS,DB) or die(json_encode(array("Registration status" => "Unable to Connect")));
		
		$username = $_GET['name'];
		
		$emailid = $_GET['email_id'];
		
		$registerno = $_GET['register_no'];
		
		$password = $_GET['password'];
		
		$mode = $_GET['mode'];
		
		if($username == '' || $password == '' || $emailid == '' || $registerno == '' || $mode == '') {
			echo json_encode(array("Registration status" => "Please fill all values"));
		}
		else {
			
			if($registerno == '*VISITOR*') {
				
				$sql = "SELECT email_id FROM user_information WHERE email_id='$emailid'";
				
				$check = mysqli_fetch_array(mysqli_query($con, $sql));
				
				if(isset($check)) {
					echo json_encode(array("Registration status" => "Registerno or Email already exist"));
				} else {
					
					$sql = "SELECT register_no mode FROM user_information WHERE mode='$mode'";
					
					$check = mysqli_query($con, $sql);
					
					$larger_count = 0;
					
					if(isset($check)) {
						
						while($row = mysqli_fetch_row($check)) {
							
							$visitor_current_count = (int)$row[0];
							
							if($larger_count < $visitor_current_count) {
								$larger_count = $visitor_current_count;
							}
						}
						
						$registerno = (string)($larger_count + 1);
						
						$sql = "INSERT INTO user_information (name,email_id,register_no,password,mode) VALUES('$username','$emailid','$registerno','$password','$mode')";
						
						if(mysqli_query($con, $sql)) {
							echo json_encode(array("Registration status" => "Successfully registered"));
						} else {
							echo json_encode(array("Registration status" => "Oops! Please try again!"));
						}
						
					} else {
						
						$registerno = (string)($larger_count + 1);
						
						$sql = "INSERT INTO user_information (name,email_id,register_no,password,mode) VALUES('$username','$emailid','$registerno','$password','$mode')";
						
						if(mysqli_query($con, $sql)) {
							echo json_encode(array("Registration status" => "Successfully registered"));
						} else {
							echo json_encode(array("Registration status" => "Oops! Please try again!"));
						}
					}
				}
				
			} else {
		
				$sql = "SELECT register_no, email_id FROM user_information WHERE register_no='$registerno' OR email_id='$emailid'";
		
				$check = mysqli_fetch_array(mysqli_query($con,$sql));
		
				if(isset($check)) {
					echo json_encode(array("Registration status" => "Registerno or Email already exist"));
				} else {
			
					$sql = "INSERT INTO user_information (name,email_id,register_no,password,mode) VALUES('$username','$emailid','$registerno','$password','$mode')";
			
					if(mysqli_query($con,$sql)) {
						echo json_encode(array("Registration status" => "Successfully registered"));
					} else {
						echo json_encode(array("Registration status" => "Oops! Please try again!"));
					}
				}
			}
		}
	} else {
		echo json_encode(array("Registration status" => "Nice try, your one step towards the hacking"));
	}
?>
