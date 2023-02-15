<?php include_once 'config.php';
$connection = getDB();
$result = array();

if ($connection){
		
	$sql = "SELECT COUNT(*) FROM COMPANY";
	$noOfCompanies = mysqli_fetch_row(mysqli_query($connection, $sql))[0];
	$result = array("status" => "success", "message" => "Fetch data successful", "noOfCompanies" => $noOfCompanies);
			
} else $result = array("status" => "failed", "message" => "Database connection failed");
echo json_encode($result, JSON_PRETTY_PRINT);