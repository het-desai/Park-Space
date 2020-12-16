<?php 
	define('HOST','localhost');

	define('USER','root');

	define('PASS','dbroot');

	define('DB','park_space_db');
	
	if($_SERVER['REQUEST_METHOD']=='POST') {
		
		$con = mysqli_connect(HOST,USER,PASS,DB) or die(json_encode(array("Login status" => "Unable to Connect")));
		
		$emailid = $_GET['email_id'];
		
		$password = $_GET['password'];
		
		$sql = "SELECT * FROM user_information WHERE email_id='$emailid' AND password='$password'";
		
		$result = mysqli_query($con,$sql);
		
		$check = mysqli_fetch_array($result);
		
		if(isset($check)) {
			echo json_encode(array("Login status" => "Success"));
		} else {
			echo json_encode(array("Login status" => "Failure"));
		}
		mysqli_close($con);
	} else {
		echo json_encode(array("Login status" => "Nice try, your one step towards the hacking"));
	}
?>
