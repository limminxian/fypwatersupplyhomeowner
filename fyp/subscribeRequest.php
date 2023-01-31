<?php
if( !empty($_POST['userID']) && 
	!empty($_POST['companyID']) && 
	!empty($_POST['subStatus']) && 
	!empty($_POST['date'])){
		
    $connection = mysqli_connect("us-cdbr-east-06.cleardb.net", "bbd12ae4b2fcc3", "df9ea7aa", "heroku_80d6ea926f679b3");
	$userID = $_POST['userID'];
	$companyID = $_POST['companyID'];
	$subStatus = $_POST['subStatus'];
	$date = $_POST['date'];


    if ($connection) {
		
		//find homeowner roles ID
		$updateUserSQL = "";
						
		$insertSubscribeSQL = "";
		
		if($subStatus == "true"){
			$updateUserSQL = "UPDATE HOMEOWNER SET SUBSCRIBE = 'NULL' WHERE ID = '".$userID."'";
			$insertSubscribeSQL = "INSERT INTO SUBSCRIBE VALUES ('".$companyID."', '".$userID."', 'NULL', '".$date."')";
		}
		else{
			$updateUserSQL = "UPDATE HOMEOWNER SET SUBSCRIBE = '".$companyID."' WHERE ID = '".$userID."'";
			$insertSubscribeSQL = "INSERT INTO SUBSCRIBE VALUES ('".$companyID."', '".$userID."', '".$date."', 'NULL')";
		}
							
		if(mysqli_query($connection, $updateUserSQL)){
			
			if(mysqli_query($connection, $insertSubscribeSQL)){
			
				echo "success";
				
			} else echo "failed inserting subscription, subscription failed";
				
		} else echo "failed updating user, edit profile failed";
		
    } else echo "Database connection failed";
} else echo "All fields are required";