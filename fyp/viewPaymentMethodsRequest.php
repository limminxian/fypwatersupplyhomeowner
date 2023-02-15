<?php include_once 'config.php';
if (!empty($_POST['userID'])
) 
{
	$connection = getDB();
    $userID = $_POST['userID'];
    $result = array();
	$cardsArr = array();
	
    if ($connection) {
		$SQL = " SELECT * FROM PAYMENTMETHODS WHERE CUSTOMER = '".$userID."'";
		$Result = mysqli_query($connection, $SQL);	
		if (mysqli_num_rows($Result) != 0) {
			while($Row = mysqli_fetch_array($Result, MYSQLI_ASSOC)){
				$id = $Row["ID"];
				$name = $Row["NAME"];
				$brand = $Row["BRAND"];
				$country = $Row["COUNTRY"];
				$address = $Row["ADDRESS"];
				$city = $Row["CITY"];
				$postalCode = $Row["POSTALCODE"];
				$cardNo = $Row["CARDNO"];
				$expMonth = $Row["EXPMONTH"];
				$expYear = $Row["EXPYEAR"];
				$cvc = $Row["CVC"];
				
				$arr = array("id" => $id, 
							"name" => $name, 
							"brand" => $brand, 
							"country" => $country, 
							"address" => $address, 
							"city" => $city, 
							"postalCode" => $postalCode, 
							"cardNo" => $cardNo, 
							"expMonth" => $expMonth, 
							"expYear" => $expYear, 
							"cvc" => $cvc
							);
							
				$cardsArr[$id] = $arr;
			}
			$result = array("status" => "success", "message" => "Fetch data successful");
			$result["cards"] = $cardsArr;
		} else $result = array("status" => "failed", "message" => "Failed to card data");					
    } else $result = array("status" => "failed", "message" => "Database connection failed");
} else $result = array("status" => "failed", "message" => "All fields are required");
echo json_encode($result, JSON_PRETTY_PRINT);
