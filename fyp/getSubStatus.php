<?php include_once 'config.php';
if (!empty($_POST['userID'])) {
	$connection = getDB();
	
    $userID = $_POST['userID'];
	$type = $_POST['type'];
	$description = $_POST['description'];

    if ($connection) {
		$SQL = "SELECT SUBSCRIBE FROM HOMEOWNER WHERE ID = '".$userID."'";
		$Result = mysqli_query($connection, $SQL);
		$sub = mysqli_fetch_row($Result)[0];
		if($sub != null){
			echo "true";
		} else echo "false";
    } else echo "Database connection failed";
} else echo "All fields are required";
