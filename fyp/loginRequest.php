<?php include_once 'config.php';

if (!empty($_POST['email']) && !empty($_POST['password'])) {
	
	$connection = getDB();
    $email = $_POST['email'];
    $password = $_POST['password'];
    $result = array();
	
    if ($connection) {
        $sql = "SELECT * FROM USERS WHERE EMAIL = '".$email."'";
        $sqlResult = mysqli_query($connection, $sql);
		
        if (mysqli_num_rows($sqlResult) != 0) {
            $row = mysqli_fetch_assoc($sqlResult);
			//var_dump($row);
			
			//find homeowner roles ID
			$getRoleSQL = "SELECT ID FROM ROLE WHERE NAME = 'homeowner'";
			$roleSQL = mysqli_query($connection, $getRoleSQL);
			$userRole = mysqli_fetch_row($roleSQL)[0];
			
			if ($row['TYPE'] == $userRole){
				
				// if ($email == $row['EMAIL']){
					
					if (password_verify($password, $row['PASSWORD'])) {
						
						if($row['STATUS'] == "ACTIVE"){ 
						
							$result = array("status" => "success", "message" => "Login successful", "userID" => $row['ID']);
						
						} else $result = array("status" => "verify", "message" => "Please verify email first", "email" => $row['EMAIL']);
						
					} else $result = array("status" => "wrong password", "message" => "Retry with correct password", "userID" => $row['ID']);
					
				// } else $result = array("status" => "failed", "message" => "Retry with correct email", "userID" => $row['ID']);
				
			} else	$result = array("status" => "failed", "message" => "Homeowner account not found");
			
        } else $result = array("status" => "failed", "message" => "Email not found");
    } else $result = array("status" => "failed", "message" => "Database connection failed");
} else $result = array("status" => "failed", "message" => "All fields are required");

echo json_encode($result, JSON_PRETTY_PRINT);
