<?php
if (!empty($_POST['userID']) && !empty($_POST['companyID'])) {
	$connection = mysqli_connect("localhost", "root", "", "fyp");
    $userID = $_POST['userID'];
	$companyID = $_POST['companyID'];
    $result = array();
	
	$name = null;
	$email = null;
	//get from admin
	$phoneNo = null;
	$street = null;
	$postalCode = null;
	$description = null;
    $noOfStars = null;
    $noOfRate = null;
	$adminID = null;
	$subscribed = null;

	//Company ID homeowner is subbed to
	$subscribeID = null;
	//address is street and postalCode
	$address = "";
	//services is all service combine in string
	$services = "";
	//serviceRates is all service rates combined in string
	$serviceRates = "";
	//array of reviews
	$reviews = array();
	//stars is all ratings added
	$stars = 0;
	$count = 0;

    if ($connection) {
		//Find if user is subscribed to this company
		$getSubSQL = "SELECT SUBSCRIBE FROM HOMEOWNER WHERE ID = '".$userID."'";
		$getSubResult = mysqli_query($connection, $getSubSQL);
		if(mysqli_num_rows($getSubResult) != 0){
			$subscribeID = mysqli_fetch_row($getSubResult)[0];
			if($subscribeID == $companyID){
				$subscribed = true;
			} else {
				$subscribed = false;
			}
		} else $result = array("status" => "failed", "message" => "Homeowner ID not found");	
		
		//get company info
		$companySQL = "SELECT COMPANY.*, USERS.EMAIL FROM COMPANY LEFT JOIN USERS ON COMPANY.ADMIN = USERS.ID WHERE COMPANY.ID = '".$companyID."'";
		$companyResult = mysqli_query($connection, $companySQL);	
		if (mysqli_num_rows($companyResult) != 0) {
			$companyRow = mysqli_fetch_assoc($companyResult);
			
			$name = $companyRow['NAME'];
			$street = $companyRow['STREET'];
			$postalCode = $companyRow['POSTALCODE'];
			$description = $companyRow['DESCRIPTION'];
			$email = $companyRow['EMAIL'];
			$phoneNo = $companyRow['NUMBER'];
			$noOfStars = $companyRow['NOOFSTAR'];
			$noOfRate = $companyRow['NOOFRATE'];
			$adminID = $companyRow['ADMIN'];
			
			$address = $street." SG:".$postalCode;
			
		} else $result = array("status" => "failed", "message" => "Failed to fetch company data");		
		
		//services and rates 
		$serviceSQL = "SELECT SERVICERATE.*, SERVICETYPE.* FROM SERVICERATE JOIN SERVICETYPE ON SERVICERATE.SERVICE = SERVICETYPE.ID INNER JOIN
						(SELECT SERVICE, MAX(EFFECTDATE) MAXDATE FROM SERVICERATE WHERE COMPANY = '".$adminID."' GROUP BY SERVICE) MT
						ON SERVICERATE.SERVICE = MT.SERVICE 
						AND SERVICERATE.EFFECTDATE = MT.MAXDATE";
		$serviceResult = mysqli_query($connection, $serviceSQL);	
		if (mysqli_num_rows($serviceResult) != 0) {
			//loop through all services
			while($serviceRow = mysqli_fetch_array($serviceResult, MYSQLI_ASSOC)){
				$serviceName = $serviceRow['NAME'];
				$serviceDescription = $serviceRow['DESCRIPTION'];
				$serviceDate = $serviceRow['EFFECTDATE'];
				$serviceRate = $serviceRow['RATE'];
				
				$services .= $serviceName.": ".$serviceDescription."\n";
				$serviceRates .= $serviceName.": ".$serviceRate."\n";
			}
			
		} else $result = array("status" => "failed", "message" => "Failed to fetch services data");		
		
		//reviews
		$reviewsSQL = "SELECT R.*, U.NAME HOMEOWNERNAME FROM REVIEWS R JOIN HOMEOWNER H ON R.HOMEOWNER = H.ID JOIN USERS U ON H.ID = U.ID WHERE COMPANY = '".$companyID."'";
		$reviewsResult = mysqli_query($connection, $reviewsSQL);	
		if (mysqli_num_rows($reviewsResult) != 0) {
			//loop through all services
			while($reviewRow = mysqli_fetch_array($reviewsResult, MYSQLI_ASSOC)){
				$homeownerName = $reviewRow['HOMEOWNERNAME'];
				$review = $reviewRow['REVIEW'];
				$reviewStars = $reviewRow['NOOFSTARS'];
				$stars += $reviewStars;
				
				$reviewArr = array("homeownerName" => $homeownerName,
								 "review" => $review,
								 "reviewStars" => $reviewStars);
								 
				$reviewName = "Review by ".$homeownerName;
				$reviews[$reviewName] = $reviewArr;
				$count++;
			}
		} else {$result = array("status" => "failed", "message" => "Failed to fetch review data"); $count++;}	
	
		//get avg stars from reviews
		$avgStars = $stars/$count;
		
		$result = array("status" => "success", "message" => "Fetch data successful", 
						"name" => $name,
						"address" => $address,
						"description" => $description,
						"email" => $email,
						"phoneNo" => $phoneNo,
						"noOfStars" => $avgStars,
						"noOfRate" => $noOfRate,
						"services" => $services,
						"serviceRates" => $serviceRates,
						"subscribed" => $subscribed,
						"reviews" => $reviews
						);
		
    } else $result = array("status" => "failed", "message" => "Database connection failed");
} else $result = array("status" => "failed", "message" => "All fields are required");
echo json_encode($result, JSON_PRETTY_PRINT);
