<?php
if( !empty($_POST['cardID'])){
		
    $connection = mysqli_connect("us-cdbr-east-06.cleardb.net", "bc292174f8cae7", "68916e25", "heroku_a43ceec7a5c075b");
	$cardID = $_POST['cardID'];
	
    if ($connection) {
		$cardSQL = "DELETE FROM PAYMENTMETHODS WHERE ID = '".$cardID."'";
		if(mysqli_query($connection, $cardSQL)){
			echo "success";
					
		} else echo "failed updating payment methods";
    } else echo "Database connection failed";
} else echo "All fields are required";