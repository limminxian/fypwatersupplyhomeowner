<?php

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require  'phpmailer/src/Exception.php';
require  'phpmailer/src/PHPMailer.php';
require  'phpmailer/src/SMTP.php';

if(!empty($_POST['userID']) ){
		
    $connection = mysqli_connect("us-cdbr-east-06.cleardb.net", "bbd12ae4b2fcc3", "df9ea7aa", "heroku_80d6ea926f679b3");
	
	$userID = $_POST['userID'];
	$email = null;
	
    if ($connection) {
		$verificationCode = rand(100000,999999);
		
		$getEmailSQL = "SELECT EMAIL FROM USERS WHERE ID = '".$userID."'";
		$getEmailResult = mysqli_query($connection, $getEmailSQL);
		if(mysqli_num_rows($getEmailResult) != 0){
			$email = mysqli_fetch_row($getEmailResult)[0];
		}else echo "Fetching email failed";
		
		
		$sql = "UPDATE USERS SET CODE = '".$verificationCode."' WHERE ID = '".$userID."' "; 
		if(mysqli_query($connection, $sql)){
			$mail = new PHPMailer(true);
		
			$mail->isSMTP();
			$mail->Host = 'smtp.gmail.com';
			$mail->SMTPAuth = true;
			$mail->Username = 'simfyp22s404@gmail.com'; //gmail name
			$mail->Password = 'krcmnobokhzcfstk'; //gmail app password
			$mail->SMTPSecure = 'ssl';
			$mail->Port = 465;
			
			$mail->setFrom('simfyp22s404@gmail.com');
			
			$mail->addAddress($email);
			
			$mail->isHTML(true);
			
			$mail->Subject = "Verification code";
			$mail->Body = "Verfication code is: ".$verificationCode;
			
			$mail->send();
			
			echo "success";
		}
		
    } else echo "Database connection failed";
} else echo "All fields are required";