<?php
$connection = mysqli_connect("localhost", "root", "", "fyp");
$ticketArr = array();
$result = array();

if ($connection) {	
	$ticketTypeSQL = "SELECT * FROM TICKETTYPE";
	$ticketTypeResult = mysqli_query($connection, $ticketTypeSQL);	
	if (mysqli_num_rows($ticketTypeResult) != 0) {
		while($ticketTypeRow = mysqli_fetch_array($ticketTypeResult, MYSQLI_ASSOC)){
			$ticketType = $ticketTypeRow['NAME'];
			array_push($ticketArr, $ticketType);
		}
		$result = array("status" => "success", "message" => "Fetch data successful");
		$result["ticketTypes"] = $ticketArr;
	} else echo $result = array("status" => "failed", "message" => "Database connection failed");
	
} else $result = array("status" => "failed", "message" => "All fields are required");
echo json_encode($result, JSON_PRETTY_PRINT);