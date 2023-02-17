<?php include_once 'config.php';
if (!empty($_POST['userID'])
	&& !empty($_POST['curr'])
	&& !empty($_POST['month'])
	&& !empty($_POST['year'])) {
		
	$connection = getDB();
    $userID = $_POST['userID'];
	$curr = $_POST['curr'];
	$month = $_POST['month'];
	$year = $_POST['year'];
    $result = array();
	$serviceBills = array();
	$totalAmount = 0;
	
	$name = null;
	$addressLine = null;
	$postalCode = null;
	$haveService = false;
	
    if ($connection) {
		//Bills
		
		if($curr == "true"){
			//Display unpaid bills for current month			
			$billsSQL = "SELECT BILL.*, SERVICETYPE.NAME SERVICENAME FROM BILL JOIN SERVICETYPE ON BILL.SERVICE = SERVICETYPE.ID 
						WHERE HOMEOWNER = '".$userID."' AND MONTH(BILLINGDATE) = '".$month."' AND YEAR(BILLINGDATE) = '".$year."' AND PAYMENT IS NULL";
			$billsResult = mysqli_query($connection, $billsSQL);	
			if (mysqli_num_rows($billsResult) != 0) {
				$haveService = true;
				
				//loop through all services
				while($billsRow = mysqli_fetch_array($billsResult, MYSQLI_ASSOC)){
					$billID = $billsRow['ID'];
					$serviceName = $billsRow['SERVICENAME'];
					$amount = $billsRow['AMOUNT'];
					$payment = $billsRow['PAYMENT'];
					$billingDate = $billsRow['BILLINGDATE'];
					$billCompany = $billsRow['COMPANY'];
					
					$SQL = "SELECT NAME FROM COMPANY WHERE ID = '".$billCompany."'";
					$Result = mysqli_query($connection, $SQL);
					$companyName = mysqli_fetch_row($Result)[0];

					$service = array("ID" => $billID, 
									 "amount" => $amount,
									 "payment" => $payment,
									 "companyName" => $companyName,
									 "billingDate" => $billingDate);
									 
					$serviceBills[$serviceName] = $service;
					$totalAmount += $amount;
				}

			} else $haveService = false;
		} else {
			//Display paid bills for previous months
			$billsSQL = "SELECT BILL.*, SERVICETYPE.NAME SERVICENAME FROM BILL JOIN SERVICETYPE ON BILL.SERVICE = SERVICETYPE.ID 
							WHERE HOMEOWNER = '".$userID."' AND MONTH(BILLINGDATE) = '".$month."' AND YEAR(BILLINGDATE) = '".$year."' AND PAYMENT IS NOT NULL";
			$billsResult = mysqli_query($connection, $billsSQL);	
			if (mysqli_num_rows($billsResult) != 0) {
				$haveService = true;
				
				//loop through all services
				while($billsRow = mysqli_fetch_array($billsResult, MYSQLI_ASSOC)){
					$billID = $billsRow['ID'];
					$serviceName = $billsRow['SERVICENAME'];
					$amount = $billsRow['AMOUNT'];
					$payment = $billsRow['PAYMENT'];
					$billingDate = $billsRow['BILLINGDATE'];
					$billCompany = $billsRow['COMPANY'];

					$SQL = "SELECT NAME FROM COMPANY WHERE ID = '".$billCompany."'";
					$Result = mysqli_query($connection, $SQL);
					$companyName = mysqli_fetch_row($Result)[0];

					$service = array("ID" => $billID, 
									 "amount" => $amount,
									 "payment" => $payment,
									 "companyName" => $companyName,
									 "billingDate" => $billingDate);
									 
					$serviceBills[$serviceName] = $service;
					$totalAmount += $amount;
				}

			} else $haveService = false;
		}
		
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
			"totalAmount" => $totalAmount
			);
			if($haveService){
				$result["serviceBills"] = $serviceBills;
			}
		} else $result = array("status" => "failed", "message" => "Failed to fetch billling data");	
		
    } else $result = array("status" => "failed", "message" => "Database connection failed");
} else $result = array("status" => "failed", "message" => "All fields are required");
echo json_encode($result, JSON_PRETTY_PRINT);
