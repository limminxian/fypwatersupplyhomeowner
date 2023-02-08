<?php
$connection = mysqli_connect("us-cdbr-east-06.cleardb.net", "bc292174f8cae7","68916e25", "heroku_a43ceec7a5c075b");
$result = array();

if ($connection){
		
	$sql = "SELECT COUNT(*) FROM COMPANY";
	$noOfCompanies = mysqli_fetch_row(mysqli_query($connection, $sql))[0];
	$result = array("status" => "success", "message" => "Fetch data successful", "noOfCompanies" => $noOfCompanies);
			
} else $result = array("status" => "failed", "message" => "Database connection failed");
echo json_encode($result, JSON_PRETTY_PRINT);