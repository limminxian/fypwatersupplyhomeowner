<?php include_once 'config.php';
if( !empty($_POST['ticketID']) &&
	// !empty($_POST['type']) &&
	!empty($_POST['description'])){
		
    $connection = getDB();
	$ticketID = $_POST['ticketID'];
	// $type = $_POST['type'];
	$description = $_POST['description'];

    if ($connection) {
		$serviceTypeSQL = "SELECT ID FROM SERVICETYPE WHERE NAME = '".$type."'";
		$serviceTypeResult = mysqli_query($connection, $serviceTypeSQL);
		if(mysqli_num_rows($serviceTypeResult) != 0){
			$serviceType = mysqli_fetch_row($serviceTypeResult)[0];
		} else $result = array("status" => "failed", "message" => "Ticket type fetch failed");			
		
		$SQL = "UPDATE TICKET SET 

					DESCRIPTION = '".$description."'
					WHERE ID = '".$ticketID."'";
		if(mysqli_query($connection, $SQL)){
			echo "success";
		} else echo "failed updating ticket";
		
    } else echo "Database connection failed";
} else echo "All fields are required";