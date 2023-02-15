<?php include_once 'config.php';
if (!empty($_POST['userID'])) 
{
	$connection = getDB();
    $userID = $_POST['userID'];
    $result = array();

	$companyID = null;
	$name = null;
	$phoneNo = null;
	$street = null;
	$postalCode = null;
	$description = null;
	$noOfStars = null;
	$noOfRate = null;
	
	$subscribeID = null;
	$subscribeStatus = null;
	$companyCount = null;
	

	
    if ($connection) {
		////Find if user is subscribed to this company
		// $getSubSQL = "SELECT SUBSCRIBE FROM HOMEOWNER WHERE ID = '".$userID."'";
		// $getSubResult = mysqli_query($connection, $getSubSQL);
		// if(mysqli_num_rows($getSubResult) != 0){
			// $subscribeID = mysqli_fetch_row($getSubResult)[0];
		// } else $result = array("status" => "failed", "message" => "Homeowner ID not found");		

		//Get count of company records
		$countSQL = "SELECT COUNT(*) FROM COMPANY";
		$countResult = mysqli_query($connection, $countSQL);
		if(mysqli_num_rows($countResult) != 0){
			$companyCount = mysqli_fetch_row($countResult)[0];
		} else $result = array("status" => "failed", "message" => "Company record count failed");	
		
		
		$result = array("status" => "success", "message" => "Fetch data successful");
		//For loop through all rows 
		for ($rowNo = 1; $rowNo <= $companyCount; $rowNo++) {
			//Get row at the row number
			$rowLimit = $rowNo - 1; 
			$companySQL = "SELECT * FROM COMPANY LIMIT ".$rowLimit.",1";
			//$companySQL = "SELECT * FROM COMPANY WHERE ID = '".$rowNo."'";
			$companyResult =  mysqli_query($connection, $companySQL);
			
			//Check if Company SQL is working
			if($companyResult){
				
				//Check if any rows found
				if (mysqli_num_rows($companyResult) != 0) {
					$companyRow = mysqli_fetch_assoc($companyResult);
					
					$companyID = $companyRow['ID'];
					$name = $companyRow['NAME'];
					$phoneNo = $companyRow['NUMBER'];
					$street = $companyRow['STREET'];
					$postalCode = $companyRow['POSTALCODE'];
					$description = $companyRow['DESCRIPTION'];
					$noOfStars = $companyRow['NOOFSTAR'];
					$noOfRate = $companyRow['NOOFRATE'];
					$logo = $companyRow['PHOTOPATH'];
					
					//Check if the user is subscribed to this company
					if($subscribeID == $companyID){
						$subscribeStatus = true;
					}
					
					//reviews
					// $stars = 0;
					// $count = 0;
					// $reviewsSQL = "SELECT NOOFSTAR FROM REVIEWS WHERE COMPANY = '".$companyID."'";
					// $reviewsResult = mysqli_query($connection, $reviewsSQL);	
					// if (mysqli_num_rows($reviewsResult) != 0) {

						// while($reviewRow = mysqli_fetch_array($reviewsResult, MYSQLI_ASSOC)){
							// $reviewStars = $reviewRow['NOOFSTARS'];
							// $stars += $reviewStars;
							// $count++;
						// }
					// } else {$noOfStars = 0; $count++;}
					
					// $avgStars = $stars/$count;
					
					//Set json object and append to the result
					$companyObj = array("companyID" => $companyID,
										"name" => $name,
										"phoneNo" => $phoneNo,
										"street" => $street,
										"postalCode" => $postalCode,
										"description" => $description,
										"noOfStars" => $noOfStars,
										"noOfRate" => $noOfRate,
										"logo" => $logo,
										"subscribeStatus" => $subscribeStatus);
					
					$companyRow = "Company row ".(string)$rowNo;
					$result[$companyRow] = $companyObj;							
				}
				
				//if no rows found, set as EOF
			} else $result = array("status" => "failed", "message" => "Failed to fetch company data");
		}					
    } else $result = array("status" => "failed", "message" => "Database connection failed");
} else $result = array("status" => "failed", "message" => "All fields are required");
echo json_encode($result, JSON_PRETTY_PRINT);
