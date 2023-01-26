<?php
if( !empty($_POST['ticketID']) &&
	!empty($_POST['type']) &&
	!empty($_POST['description'])){
		
    $connection = mysqli_connect("localhost", "root", "", "fyp");
	$ticketID = $_POST['ticketID'];
	$type = $_POST['type'];
	$description = $_POST['description'];

    if ($connection) {
		$ticketTypeSQL = "SELECT ID FROM TICKETTYPE WHERE NAME = '".$type."'";
		$ticketTypeResult = mysqli_query($connection, $ticketTypeSQL);
		if(mysqli_num_rows($ticketTypeResult) != 0){
			$ticketType = mysqli_fetch_row($ticketTypeResult)[0];
		} else $result = array("status" => "failed", "message" => "Ticket type fetch failed");			
		
		$SQL = "UPDATE TICKET SET 
					TYPE = '".$ticketType."',
					DESCRIPTION = '".$description."'
					WHERE ID = '".$ticketID."'";
		if(mysqli_query($connection, $SQL)){
			echo "success";
		} else echo "failed updating ticket";
		
    } else echo "Database connection failed";
} else echo "All fields are required";