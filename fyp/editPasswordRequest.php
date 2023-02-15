<?php include_once 'config.php';
if (!empty($_POST['userID']) && !empty($_POST['oldPassword']) && !empty($_POST['newPassword']) && !empty($_POST['cmfNewPassword'])) {
	$connection = getDB();
    
	$result = array();
	$userID = $_POST['userID'];
    $oldPassword = $_POST['oldPassword'];
	$newPassword = $_POST['newPassword'];
	$cmfNewPassword = $_POST['cmfNewPassword'];
	
    if ($connection) {
        $userSQL = "SELECT * FROM USERS WHERE ID = '".$userID."'";
        $userResult = mysqli_query($connection, $userSQL);
		
		if (mysqli_num_rows($userResult) != 0) {
			$row = mysqli_fetch_assoc($userResult);
			
			if($newPassword == $cmfNewPassword){

				if(password_verify($oldPassword, $row['PASSWORD'])){
					$newPassword = password_hash($newPassword, PASSWORD_DEFAULT);
					$updatePasswordSQL = "UPDATE USERS SET PASSWORD = '".$newPassword."' WHERE ID = '".$userID."'";
					
					if(mysqli_query($connection, $updatePasswordSQL)){
							echo "success";
					} else echo "failed to update password";
					
				} else echo "wrong password";				
				
			} else echo "re-enter password do not match";
			
		} else echo "User ID not found";	
		
    } else echo "Database connection failed";
	
} else echo "All fields are required";