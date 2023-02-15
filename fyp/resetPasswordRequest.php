<?php include_once 'config.php';
if (!empty($_POST['userID']) &&
 !empty($_POST['verifyCode']) && 
 !empty($_POST['newPassword']) && 
 !empty($_POST['rePassword'])) {
	$connection = getDB();
    
	$result = array();
	$userID = $_POST['userID'];
    $verifyCode = $_POST['verifyCode'];
	$newPassword = $_POST['newPassword'];
	$rePassword = $_POST['rePassword'];
	
    if ($connection) {
        $userSQL = "SELECT * FROM USERS WHERE ID = '".$userID."'";
        $userResult = mysqli_query($connection, $userSQL);
		
		if (mysqli_num_rows($userResult) != 0) {
			$row = mysqli_fetch_assoc($userResult);
			
			if($newPassword == $rePassword){

				if($row["CODE"]==$verifyCode){
					$newPassword = password_hash($newPassword, PASSWORD_DEFAULT);
					$updatePasswordSQL = "UPDATE USERS SET PASSWORD = '".$newPassword."' WHERE ID = '".$userID."'";
					
					if(mysqli_query($connection, $updatePasswordSQL)){
							echo "success";
					} else echo "failed to update password";
					
				} else echo "wrong verification code";				
				
			} else echo "re-enter password do not match";
			
		} else echo "User ID not found";	
		
    } else echo "Database connection failed";
	
} else echo "All fields are required";