<?php include_once 'config.php';;

if (!empty($_POST['cardID'])) {
	$connection = getDB();
    $cardID = $_POST['cardID'];
    $result = array();
	
	$cardID = $_POST['cardID'];
	$cardNo = null;
	$name = null;
	$cvc = null;
    $address = null;
	$country = null;
    $postalCode = null;
    $brand = null;
	$expMonth = null;
	$expYear = null;
	
    if ($connection) { 
		$SQL = "SELECT * FROM PAYMENTMETHODS WHERE ID = '".$cardID."'";
		$Result = mysqli_query($connection, $SQL);
		
		if (mysqli_num_rows($Result) != 0) {
			$Row = mysqli_fetch_assoc($Result);
			
			$cardNo = $Row['CARDNO'];
			$name = $Row['NAME'];
			$cvc = $Row['CVC'];
			$address = $Row['ADDRESS'];
			$country = $Row['COUNTRY'];
			$postalCode = $Row['POSTALCODE'];
			$brand = $Row['BRAND'];
			$expMonth = $Row['EXPMONTH'];
			$expYear = $Row['EXPYEAR'];
			
			$result = array("status" => "success", "message" => "Fetch data successful", 
						"cardNo" => $cardNo,
						"name" => $name,
						"cvc" => $cvc,
						"address" => $address,
						"country" => $country,
						"postalCode" => $postalCode,
						"brand" => $brand,
						"expMonth" => $expMonth,
						"expYear" => $expYear);
						
		} else $result = array("status" => "failed", "message" => "Failed to fetch payment method data");				
    } else $result = array("status" => "failed", "message" => "Database connection failed");
} else $result = array("status" => "failed", "message" => "All fields are required");
echo json_encode($result, JSON_PRETTY_PRINT);
