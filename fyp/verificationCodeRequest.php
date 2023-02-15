<?php include_once 'config.php';

// use PHPMailer\PHPMailer\PHPMailer;
// use PHPMailer\PHPMailer\Exception;

// require  'phpmailer/src/Exception.php';
// require  'phpmailer/src/PHPMailer.php';
// require  'phpmailer/src/SMTP.php';
$result = array();
if(!empty($_POST['userID']) && !empty($_POST['email'])){
		
    $connection = getDB();
	
	$userID = $_POST['userID'];
	$email = $_POST['email'];
	
    if ($connection) {
		$verificationCode = rand(100000,999999);
		
		if($email=="null"){
			$getEmailSQL = "SELECT EMAIL FROM USERS WHERE ID = '".$userID."'";
			$getEmailResult = mysqli_query($connection, $getEmailSQL);
			if(mysqli_num_rows($getEmailResult) != 0){
				$email = mysqli_fetch_row($getEmailResult)[0];
			}else $result = array("status" => "failed", "message" => "email fetch failed");
			$sql = "UPDATE USERS SET CODE = '".$verificationCode."' WHERE ID = '".$userID."' "; 

			if(mysqli_query($connection, $sql)){
			
			$result = array("status" => "success", "message" => "Fetch data successful",
			"email" => $email,
			"verificationCode" => $verificationCode);
			}	
			
		} else {
			$sql = "UPDATE USERS SET CODE = '".$verificationCode."' WHERE EMAIL = '".$email."' "; 

			if(mysqli_query($connection, $sql)){
			
			$result = array("status" => "success", "message" => "Fetch data successful",
			"email" => $email,
			"verificationCode" => $verificationCode);
			}
		}
		
		
		
		
    } else echo $result = array("status" => "failed", "message" => "Database connection failed");
} else $result = array("status" => "failed", "message" => "All fields are required");
echo json_encode($result, JSON_PRETTY_PRINT);