<?php include_once 'config.php';
if (!empty($_POST['userID']) 
	&& !empty($_POST['type'])
	&& !empty($_POST['date'])
	&& !empty($_POST['description'])) {
	$connection = getDB();
	
    $userID = $_POST['userID'];
	$type = $_POST['type'];
	$date = $_POST['date'];
	$description = $_POST['description'];

    if ($connection) {
		$SQL = "SELECT ID FROM SERVICETYPE WHERE NAME = '".$type."'";
		$Result = mysqli_query($connection, $SQL);
		$typeID = mysqli_fetch_row($Result)[0];
		
		//get least workload customerservice staff
		$custID = null;
		$csSQL = 	"SELECT U.ID FROM 
					USERS U JOIN STAFF S ON U.ID = S.ID 
					JOIN ROLE R ON U.TYPE = R.ID 
					WHERE R.NAME = 'customerservice'
					ORDER BY S.WORKLOAD ASC LIMIT 1";
		$csResult = mysqli_query($connection, $csSQL);
		if(mysqli_num_rows($csResult) != 0){
			$custID = mysqli_fetch_row($csResult)[0];
		} else echo "customer id fetch failed";
		
		//services and rates 
		$raiseTicketSQL = "INSERT INTO TICKET ( HOMEOWNER, TYPE, CUSTOMERSERVICE, STATUS, DESCRIPTION, SERVICEDATE) 
							VALUES (".$userID.", ".$typeID.", ".$custID.", 'pending','".$description."','".$date."')";
		$raiseTicketResult = mysqli_query($connection, $raiseTicketSQL);	
		if ($raiseTicketResult) {
			echo "success";
		} else echo "failed updating ticket, raise ticket failed";	
		
    } else echo "Database connection failed";
} else echo "All fields are required";
