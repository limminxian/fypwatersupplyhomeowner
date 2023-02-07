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
	$custID = null;


    if ($connection) {
		
		//find homeowner roles ID
		$updateUserSQL = "";				
		$insertSubscribeSQL = "";
		//insert ticket and set ticket id 
		// $ticketSQL = "INSERT INTO TICKET (HOMEOWNER, CUSTOMERSERIVCE, STATUS, )";
		//insert sub and set ticket to max ticket id
		
		
		
		
		if($subStatus == "true"){
			//get least workload customerservice staff
			$csSQL = 	"SELECT U.ID FROM 
						USERS U JOIN STAFF S ON U.ID = S.ID 
						JOIN ROLE R ON U.TYPE = R.ID 
						WHERE R.NAME = 'customerservice'
						ORDER BY S.WORKLOAD ASC LIMIT 1";
			$csResult = mysqli_query($connection, $csSQL);
			if(mysqli_num_rows($csResult) != 0){
				$custID = mysqli_fetch_row($csResult)[0];
			} else echo "customer id fetch failed";
			
			//create a ticket for uninstallation
			$raiseTicketSQL = "INSERT INTO TICKET ( HOMEOWNER, TYPE, CUSTOMERSERVICE, STATUS, DESCRIPTION) 
								VALUES (".$userID.", 'uninstallation', ".$custID.", 'open','homeowner uninstallation when unsubscribed')";
			$raiseTicketResult = mysqli_query($connection, $raiseTicketSQL);	
			if ($raiseTicketResult) {
				echo "success";
			} else echo "failed updating ticket, raise ticket failed";
			
			//get ticket id to set in subscribe table
			$maxID = null;
			$maxIDSQL = "SELECT MAX(ID) FROM TICKET";
			$maxIDResult = mysqli_query($connection, $maxIDSQL);
			if(mysqli_num_rows($maxIDResult) != 0){
				$maxID = mysqli_fetch_row($maxIDResult)[0];
			} else echo "failed to get max ticket id";			
			
			//set homeowner to unsubscribed
			$updateUserSQL = "UPDATE HOMEOWNER SET SUBSCRIBE = NULL WHERE ID = '".$userID."'";
			$insertSubscribeSQL = "INSERT INTO SUBSCRIBE VALUES ('".$companyID."', '".$userID."', 'NULL', '".$date."', '".$maxID."')";
		}
		else{
			//get least workload customerservice staff
			$csSQL = 	"SELECT U.ID FROM 
						USERS U JOIN STAFF S ON U.ID = S.ID 
						JOIN ROLE R ON U.TYPE = R.ID 
						WHERE R.NAME = 'customerservice'
						ORDER BY S.WORKLOAD ASC LIMIT 1";
			$csResult = mysqli_query($connection, $csSQL);
			if(mysqli_num_rows($csResult) != 0){
				$custID = mysqli_fetch_row($csResult)[0];
			} else echo "customer id fetch failed";
			
			//create a ticket for installation
			$raiseTicketSQL = "INSERT INTO TICKET ( HOMEOWNER, TYPE, CUSTOMERSERVICE, STATUS, DESCRIPTION) 
								VALUES (".$userID.", ''installation, ".$custID.", 'open','homeowner installation when subscribed')";
			$raiseTicketResult = mysqli_query($connection, $raiseTicketSQL);	
			if ($raiseTicketResult) {
				echo "success";
			} else echo "failed updating ticket, raise ticket failed";
			
			//get ticket id to set in subscribe table
			$maxID = null;
			$maxIDSQL = "SELECT MAX(ID) FROM TICKET";
			$maxIDResult = mysqli_query($connection, $maxIDSQL);
			if(mysqli_num_rows($maxIDResult) != 0){
				$maxID = mysqli_fetch_row($maxIDResult)[0];
			} else echo "failed to get max ticket id";	
			
			//set homeowner to subscribed
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