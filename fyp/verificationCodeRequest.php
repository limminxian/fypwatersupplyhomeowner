<?php
if(!empty($_POST['userID'])){
		
    $connection = mysqli_connect("us-cdbr-east-06.cleardb.net", "bbd12ae4b2fcc3", "df9ea7aa", "heroku_80d6ea926f679b3");
	
	$userID = $_POST['userID'];
	
    if ($connection) {
		$verificationCode = rand(100000,999999);
		$sql = "UPDATE USERS SET CODE = '".$verificationCode."' WHERE ID = '".$userID."' "; 
		if(mysqli_query($connection, $sql)){
			echo "success";
		}
		
    } else echo "Database connection failed";
} else echo "All fields are required";