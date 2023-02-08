<?php
$connection = mysqli_connect("us-cdbr-east-06.cleardb.net", "bc292174f8cae7","68916e25", "heroku_a43ceec7a5c075b");
$ticketArr = array();
$result = array();

if ($connection) {	
	$serviceTypeSQL = "SELECT * FROM SERVICETYPE";
	$serviceTypeResult = mysqli_query($connection, $serviceTypeSQL);	
	if (mysqli_num_rows($serviceTypeResult) != 0) {
		while($serviceTypeRow = mysqli_fetch_array($serviceTypeResult, MYSQLI_ASSOC)){
			$serviceType = $serviceTypeRow['NAME'];
			array_push($ticketArr, $serviceType);
		}
		$result = array("status" => "success", "message" => "Fetch data successful");
		$result["serviceTypes"] = $ticketArr;
	} else echo $result = array("status" => "failed", "message" => "Database connection failed");
	
} else $result = array("status" => "failed", "message" => "All fields are required");
echo json_encode($result, JSON_PRETTY_PRINT);