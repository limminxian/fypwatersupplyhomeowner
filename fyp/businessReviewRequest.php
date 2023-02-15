<?php include_once 'config.php';
if( !empty($_POST['homeownerID']) && 
	!empty($_POST['companyID']) &&
	!empty($_POST['review']) && 
	!empty($_POST['noOfStars'])){
		
    $connection = getDB();
	$homeownerID = $_POST['homeownerID'];
    $companyID = $_POST['companyID'];
	$review = $_POST['review'];
    $noOfStars = $_POST['noOfStars'];
	$noOfRate = null;
	 
    if ($connection) {
		
		$reviewSQL = "INSERT INTO REVIEWS (HOMEOWNER, COMPANY, REVIEW, NOOFSTAR) 
						VALUES('".$homeownerID."', '".$companyID."', '".$review."', '".$noOfStars."')";
		$reviewResult = mysqli_query($connection, $reviewSQL);
		if($reviewResult){

		} else echo "failed to insert review";
		
		$companySQL = "SELECT NOOFSTAR, NOOFRATE FROM COMPANY WHERE ID = '".$companyID."'";
		$companyResult = mysqli_query($connection, $companySQL);
		if (mysqli_num_rows($companyResult) != 0) {
			$companyRow = mysqli_fetch_assoc($companyResult);
			
			$prevStars = $companyRow['NOOFSTAR'];
			$noOfRate = $companyRow['NOOFRATE'];
		}
		$companyStars = $prevStars*$noOfRate;
		$noOfRate += 1;
		$companyStars += $noOfStars;
		$companyStars /= $noOfRate;
		
		$updateCompanySQL = "UPDATE COMPANY SET NOOFSTAR = '".$companyStars."', NOOFRATE = '".$noOfRate."' WHERE ID = '".$companyID."'";
		$updateCompanyResult = mysqli_query($connection, $updateCompanySQL);
		if($updateCompanyResult){
			echo "success";
		} else echo "failed to insert company";
		
    } else echo "Database connection failed";
} else echo "All fields are required";