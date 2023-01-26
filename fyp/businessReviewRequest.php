<?php
if( !empty($_POST['homeownerID']) && 
	!empty($_POST['companyID']) &&
	!empty($_POST['review']) && 
	!empty($_POST['noOfStars'])){
		
    $connection = mysqli_connect("localhost", "root", "", "fyp");
	$homeownerID = $_POST['homeownerID'];
    $companyID = $_POST['companyID'];
	$review = $_POST['review'];
    $noOfStars = $_POST['noOfStars'];
	 
    if ($connection) {
		
		$reviewSQL = "INSERT INTO REVIEWS (HOMEOWNER, COMPANY, REVIEW, NOOFSTARS) 
						VALUES('".$homeownerID."', '".$companyID."', '".$review."', '".$noOfStars."')";
		$reviewResult = mysqli_query($connection, $reviewSQL);
		if($reviewResult){
			echo "success";
		} else echo "failed to insert review";
		
    } else echo "Database connection failed";
} else echo "All fields are required";