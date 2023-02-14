<?php
if( !empty($_POST['userID']) && 
	!empty($_POST['companyID']) && 
	!empty($_POST['date'])){
		
    $connection = mysqli_connect("us-cdbr-east-06.cleardb.net", "bc292174f8cae7", "68916e25", "heroku_a43ceec7a5c075b");
	$userID = $_POST['userID'];
	$companyID = $_POST['companyID'];
	$date = $_POST['date'];
	$custID = null;
	$workLoad = null;
	$result = array();

    if ($connection) {
		//WORKLOAD
		$csSQL = 	"SELECT U.ID custID, S.WORKLOAD workLoad FROM 
					USERS U JOIN STAFF S ON U.ID = S.ID 
					JOIN ROLE R ON U.TYPE = R.ID 
					WHERE R.NAME = 'customerservice'
					ORDER BY S.WORKLOAD ASC LIMIT 1";
		$csResult = mysqli_query($connection, $csSQL);
		if (mysqli_num_rows($csResult) != 0) {
			$Row = mysqli_fetch_assoc($csResult);
			$custID = $Row['custID'];
			$workLoad = $Row['workLoad'];
			
		} else echo "customer id fetch failed";
		
		//SERVICE ID
		$serviceType = null;
		$serviceTypeSQL = "SELECT ID FROM SERVICETYPE WHERE NAME = 'uninstallation'";
		$serviceTypeResult = mysqli_query($connection, $serviceTypeSQL);
		if(mysqli_num_rows($serviceTypeResult) != 0){
			$serviceType = mysqli_fetch_row($serviceTypeResult)[0];
		} else echo "failed to service type id";			
	
		//CREATE TICKET
		$raiseTicketSQL = "INSERT INTO TICKET ( HOMEOWNER, TYPE, CUSTOMERSERVICE, STATUS, DESCRIPTION, SERVICEDATE) 
							VALUES ('".$userID."', '".$serviceType."', '".$custID."', 'open','homeowner uninstallation when unsubscribed', '".$date."')";
		$raiseTicketResult = mysqli_query($connection, $raiseTicketSQL);	
		if ($raiseTicketResult) {
		} else echo "failed updating ticket, raise ticket failed";
		
		//INCREMENT WORKLOAD
		$workLoad += 1;
		$increWLSQL = "UPDATE STAFF SET WORKLOAD = '".$workLoad."' WHERE ID = '".$custID."'";
		$increWLResult = mysqli_query($connection, $increWLSQL);	
		if ($increWLResult) {
		} else echo "failed incrementing workload failed";
		
		//TICKET ID
		$maxID = null;
		$maxIDSQL = "SELECT MAX(ID) FROM TICKET";
		$maxIDResult = mysqli_query($connection, $maxIDSQL);
		if(mysqli_num_rows($maxIDResult) != 0){
			$maxID = mysqli_fetch_row($maxIDResult)[0];
		} else echo "failed to get max ticket id";			
		
		//SUBSCRIBE
		$dateSQL = "SELECT DATE FROM SUBSCRIBE WHERE DATE = '".$date."' AND HOMEOWNER = '".$userID."'";
		$updateUserSQL = "UPDATE HOMEOWNER SET SUBSCRIBE = NULL WHERE ID = '".$userID."'";
		$insertSubscribeSQL = "INSERT INTO SUBSCRIBE VALUES ('".$companyID."', '".$userID."', '".$date."','unsubscribe', '".$maxID."')";
		
		if(date_format($date, "Y-m-d") < date("Y-m-d")){
			$dateResult = mysqli_query($connection, $dateSQL);
			if(mysqli_num_rows($dateResult) == 0){
				if(mysqli_query($connection, $updateUserSQL)){
						if(mysqli_query($connection, $insertSubscribeSQL)){
							echo "success";
						} else echo "failed inserting subscription, subscription failed";
				} else echo "failed updating user, edit profile failed";
			} else echo "Date has already been chosen, please choose another date";
		} else echo "Please select a valid date";
	} else echo "Database connection failed";
} else echo "All fields are required";