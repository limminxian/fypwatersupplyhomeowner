<?php
if(!empty($_POST['email']) && !empty($_POST['verificationCode'])){
		
    $connection = mysqli_connect("us-cdbr-east-06.cleardb.net", "bbd12ae4b2fcc3", "df9ea7aa", "heroku_80d6ea926f679b3");
	
	$email = $_POST['email'];
    $verificationCode = $_POST['verificationCode'];
	$userID = null;
	$code = null;
	
    if ($connection) {
		// find the user ID of homeowner
		// $getUserIDSQL = "SELECT ID FROM USERS WHERE EMAIL = '".$email."'";
		// $userIDSQL = mysqli_query($connection, $getUserIDSQL);
		// $userID = mysqli_fetch_row($userIDSQL)[0];
		
		//find the verification code of homeowner, verify
		$getCodeSQL = "SELECT CODE FROM USERS WHERE EMAIL = '".$email."'";
		$codeSQL = mysqli_query($connection, $getCodeSQL);
		$code = mysqli_fetch_row($codeSQL)[0];
		
		//set homeowner account status as active
		if($verificationCode == $code){
			
			$sql = "UPDATE USERS SET STATUS = 'ACTIVE' WHERE ID = '".$userID."' "; 
			if(mysqli_query($connection, $sql)){
				echo $code;
			}
			
		} else echo "wrong verification code";				
    } else echo "Database connection failed";
} else echo "All fields are required";