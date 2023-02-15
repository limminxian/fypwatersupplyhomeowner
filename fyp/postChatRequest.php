<?php include_once 'config.php';
if( !empty($_POST['userID']) && 
	!empty($_POST['ticketID']) && 
	!empty($_POST['text'])){
		
    $connection = getDB();
	$userID = $_POST['userID'];
	$ticketID = $_POST['ticketID'];
	$text = $_POST['text'];

    if ($connection) {
		
		$chatSQL = "INSERT INTO CHAT VALUES (current_timestamp(), '".$ticketID."', '".$userID."', '".$text."')";
		if(mysqli_query($connection, $chatSQL)){
			
				echo "success";
							
		} else echo "failed inserting chat";
		
		//find homeowner roles ID
		// $updateUserSQL = "";
						
		// $insertSubscribeSQL = "";
		
		// if($subStatus == "true"){
			// $updateUserSQL = "UPDATE HOMEOWNER SET SUBSCRIBE = NULL WHERE ID = '".$userID."'";
			// $insertSubscribeSQL = "INSERT INTO SUBSCRIBE VALUES ('".$companyID."', '".$userID."', 'NULL', '".$date."')";
		// }
		// else{
			// $updateUserSQL = "UPDATE HOMEOWNER SET SUBSCRIBE = '".$companyID."' WHERE ID = '".$userID."'";
			// $insertSubscribeSQL = "INSERT INTO SUBSCRIBE VALUES ('".$companyID."', '".$userID."', '".$date."', 'NULL')";
		// }
							
		// if(mysqli_query($connection, $updateUserSQL)){
			
			// if(mysqli_query($connection, $insertSubscribeSQL)){
			
				// echo "success";
				
			// } else echo "failed inserting subscription, subscription failed";
				
		// } else echo "failed updating user, edit profile failed";
		
    } else echo "Database connection failed";
} else echo "All fields are required";