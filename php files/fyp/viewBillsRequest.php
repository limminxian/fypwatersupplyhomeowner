<?php
if (!empty($_POST['userID'])
	&& !empty($_POST['month'])
	&& !empty($_POST['year'])) {
		
	$connection = mysqli_connect("localhost", "root", "", "fyp");
    $userID = $_POST['userID'];
	$month = $_POST['month'];
	$year = $_POST['year'];
    $result = array();
	$serviceBills = array();
	$totalAmount = 0;
	
	$name = null;
	$addressLine = null;
	$postalCode = null;
	
	
    if ($connection) {
		//Bills
		$billsSQL = "SELECT BILL.*, SERVICETYPE.NAME SERVICENAME FROM BILL JOIN SERVICETYPE ON BILL.SERVICE = SERVICETYPE.ID 
						WHERE HOMEOWNER = '".$userID."' AND MONTH(PAYMENTDATE) = '".$month."' AND YEAR(PAYMENTDATE) = '".$year."'";
		$billsResult = mysqli_query($connection, $billsSQL);	
		if (mysqli_num_rows($billsResult) != 0) {
			//loop through all services
			while($billsRow = mysqli_fetch_array($billsResult, MYSQLI_ASSOC)){
				$serviceName = $billsRow['SERVICENAME'];
				$amount = $billsRow['AMOUNT'];
				$payment = $billsRow['PAYMENT'];
				$paymentDate = $billsRow['PAYMENTDATE'];

				$service = array("amount" => $amount,
								 "payment" => $payment,
								 "paymentDate" => $paymentDate);
								 
				$serviceBills[$serviceName] = $service;
				$totalAmount += $amount;
			}

		} else $result = array("status" => "failed", "message" => "Failed to fetch bills data");	
		
		//get payment info
		$userSQL = "SELECT U.NAME, H.* FROM USERS U JOIN HOMEOWNER H ON U.ID = H.ID WHERE U.ID = '".$userID."'";
		$userResult = mysqli_query($connection, $userSQL);
		if(mysqli_num_rows($userResult)){
			$userRow = mysqli_fetch_assoc($userResult);
			$name = $userRow['NAME'];
			$street = $userRow['STREET'];
			$blkno = $userRow['BLOCKNO'];
			$unitno = $userRow['UNITNO'];
			$postalCode = $userRow['POSTALCODE'];
			
			$addressLine = $street." ".$blkno." ".$unitno;
			$result = array("status" => "success", "message" => "Fetch data successful", 
			"name" => $name, 
			"addressLine" => $addressLine, 
			"postalCode" => $postalCode,
			"serviceBills" => $serviceBills,
			"totalAmount" => $totalAmount
			);
		} else $result = array("status" => "failed", "message" => "Failed to fetch payment data");	
		
		
    } else $result = array("status" => "failed", "message" => "Database connection failed");
} else $result = array("status" => "failed", "message" => "All fields are required");
echo json_encode($result, JSON_PRETTY_PRINT);
