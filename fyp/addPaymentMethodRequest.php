<?php include_once 'config.php';
if( !empty($_POST['userID']) && 
	!empty($_POST['cardNo']) && 
	!empty($_POST['name']) && 
	!empty($_POST['expiration']) && 
	!empty($_POST['cvc']) && 
	!empty($_POST['address']) && 
	!empty($_POST['country']) && 
	!empty($_POST['postalCode']) && 
	!empty($_POST['brand'])){
		
    $connection = getDB();
	$userID = $_POST['userID'];
	$cardNo = $_POST['cardNo'];
	$name = $_POST['name'];
    $expiration = $_POST['expiration'];
	$cvc = $_POST['cvc'];
    $address = $_POST['address'];
	$country = $_POST['country'];
    $postalCode = $_POST['postalCode'];
    $brand = $_POST['brand'];
	$expMonth = null;
	$expYear = null;
	
    if ($connection) {
		$expRegex = '/^[1-3][0-9]\/[0-9]{2}$/';
		if(preg_match($expRegex, $expiration)){
			$expMonth = substr($expiration, 0,2);
			$expYear = substr($expiration, -2);
			if($expMonth < 32){
				$cardSQL = "INSERT INTO PAYMENTMETHODS (CUSTOMER, NAME, COUNTRY, ADDRESS, CITY, POSTALCODE, BRAND, EXPMONTH, EXPYEAR, CVC, CARDNO) 
							VALUES ('".$userID."',
									'".$name."',
									'".$country."',
									'".$address."',
									'".$country."',
									'".$postalCode."',
									'".$brand."',																
									'".$expMonth."',
									'".$expYear."',
									'".$cvc."',
									'".$cardNo."')";
				// $cardSQL = "UPDATE PAYMENTMETHODS SET
							// CARDNO = '".$cardNo."',
							// NAME = '".$name."',
							// EXPMONTH = '".$expMonth."',
							// EXPYEAR = '".$expYear."',
							// CVC = '".$cvc."',
							// ADDRESS = '".$address."',
							// COUNTRY = '".$country."',
							// POSTALCODE = '".$postalCode."',
							// BRAND = '".$brand."',
							// WHERE CUSTOMER = '".$userID."'";
				if(mysqli_query($connection, $cardSQL)){
					echo "success";
					
				} else echo "failed updating payment methods";
			} else echo "Please enter a valid date";
		} else echo "Please enter in the correct format";
    } else echo "Database connection failed";
} else echo "All fields are required";