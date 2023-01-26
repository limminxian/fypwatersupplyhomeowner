<?php
if (!empty($_POST['userID']) 
	&& !empty($_POST['type'])
	&& !empty($_POST['description'])) {
	$connection = mysqli_connect("localhost", "root", "", "fyp");
	
    $userID = $_POST['userID'];
	$type = $_POST['type'];
	$description = $_POST['description'];

    if ($connection) {
		$SQL = "SELECT ID FROM TICKETTYPE WHERE NAME = '".$type."'";
		$Result = mysqli_query($connection, $SQL);
		$typeID = mysqli_fetch_row($Result)[0];
		
		//services and rates 
		$raiseTicketSQL = "INSERT INTO TICKET ( HOMEOWNER, TYPE, DESCRIPTION) 
							VALUES (".$userID.", ".$typeID.", '".$description."')";
		$raiseTicketResult = mysqli_query($connection, $raiseTicketSQL);	
		if ($raiseTicketResult) {
			echo "success";
		} else echo "failed updating ticket, raise ticket failed";	
		
    } else echo "Database connection failed";
} else echo "All fields are required";
