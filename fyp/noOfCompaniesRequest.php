<?php
$connection = mysqli_connect("us-cdbr-east-06.cleardb.net", "bbd12ae4b2fcc3", "df9ea7aa", "heroku_80d6ea926f679b3");
$result = array();

if ($connection){
		
	$sql = "SELECT COUNT(*) FROM COMPANY";
	$noOfCompanies = mysqli_fetch_row(mysqli_query($connection, $sql))[0];
	$result = array("status" => "success", "message" => "Fetch data successful", "noOfCompanies" => $noOfCompanies);
			
} else $result = array("status" => "failed", "message" => "Database connection failed");
echo json_encode($result, JSON_PRETTY_PRINT);