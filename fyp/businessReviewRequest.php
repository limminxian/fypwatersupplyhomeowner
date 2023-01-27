<?php
if( !empty($_POST['homeownerID']) && 
	!empty($_POST['companyID']) &&
	!empty($_POST['review']) && 
	!empty($_POST['noOfStars'])){
		
    $connection = mysqli_connect("us-cdbr-east-06.cleardb.net", "bbd12ae4b2fcc3", "df9ea7aa", "heroku_80d6ea926f679b3");
	$homeownerID = $_POST['homeownerID'];
    $companyID = $_POST['companyID'];
	$review = $_POST['review'];
    $noOfStars = $_POST['noOfStars'];
	
	$noOfStars = null;
	$noOfRate = null;
	 
    if ($connection) {
		
		$companySQL = "SELECT NOOFSTARS, NOOFRATE FROM COMPANY WHERE ID = '".$companyID."'";
		$companyResult = mysqli_query($connection, $companySQL);
		if (mysqli_num_rows($companyResult) != 0) {
			$companyRow = mysqli_fetch_assoc($companyResult);
			
			$noOfStars = $companyRow['NOOFSTARS'];
			$noOfRate = $companyRow['NOOFRATE'];
		}
		$companyStars = $noOfStars*$noOfRate;
		$noOfRate += 1;
		$companyStars += $noOfRate;
		$companyStars /= $noOfRate;
				
		$reviewSQL = "INSERT INTO REVIEWS (HOMEOWNER, COMPANY, REVIEW, NOOFSTARS) 
						VALUES('".$homeownerID."', '".$companyID."', '".$review."', '".$noOfStars."')";
		$reviewResult = mysqli_query($connection, $reviewSQL);
		if($reviewResult){

		} else echo "failed to insert review";
		
		$updateCompanySQL = "INSERT INTO COMPANY (NOOFSTARS, NOOFRATE) VALUES('".$companyStars."', '".$noOfRate."')";
		$updateCompanyResult = mysqli_query($connection, $updateCompanySQL);
		if($updateCompanyResult){
			echo "success";
		} else echo "failed to insert company";
		
    } else echo "Database connection failed";
} else echo "All fields are required";