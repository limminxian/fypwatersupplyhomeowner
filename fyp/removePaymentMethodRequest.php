<?php include_once 'config.php';
if(!empty($_POST['cardID'])){
		
    $connection = getDB();
	$cardID = $_POST['cardID'];
	
    if ($connection) {
		$cardSQL = "DELETE FROM PAYMENTMETHODS WHERE ID = '".$cardID."'";
		if(mysqli_query($connection, $cardSQL)){
			echo "success";
					
		} else echo "failed updating payment methods";
    } else echo "Database connection failed";
} else echo "All fields are required";