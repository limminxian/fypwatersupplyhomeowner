<?php include_once 'config.php'; 
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require  'phpmailer/src/Exception.php';
require  'phpmailer/src/PHPMailer.php';
require  'phpmailer/src/SMTP.php';

// $emailname = 'watersupply02@gmail.com';
// $passwordname = 'ughqjjdtgswonvuj';
$emailname = 'watersupply03@gmail.com';
$passwordname = 'uxgaejqdrruvnspk';
// $emailname = 'watersupply04@gmail.com';
// $passwordname = 'tqyicfdtjxprgjjw';
// $emailname = 'watersupply131@gmail.com';
// $passwordname = 'lseyfssimrjhsqda';


if( !empty($_POST['email']) &&
	!empty($_POST['message'])){
		
	// if(isset($_POST["send"])){
		$mail = new PHPMailer(true);
		
		$mail->isSMTP();
		$mail->Host = 'smtp.gmail.com';
		$mail->SMTPAuth = true;
		$mail->Username = $emailname; //gmail name
		$mail->Password = $passwordname; //gmail app password
		$mail->SMTPSecure = 'ssl';
		$mail->Port = 465;
		
		$mail->setFrom($emailname);	//gmail name
		
		$mail->addAddress($_POST["email"]);
		
		$mail->isHTML(true);
		
		$mail->Subject = 'Verification Code';
		$mail->Body = $_POST["message"];
		
		$mail->send();
		
		$mail->SMTPDebug = true;
		
		echo "success";
	
	
} else echo "All fields are required";

?>