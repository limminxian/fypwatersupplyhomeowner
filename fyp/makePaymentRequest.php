<?php include_once 'config.php';
if( !empty($_POST['billIDSize']) &&
	!empty($_POST['paymentDate']) &&
	!empty($_POST['cardID'])){
		
    $connection = getDB();
	$paymentDate = $_POST['paymentDate'];
	$cardID = $_POST['cardID'];
	$billIDSize = $_POST['billIDSize'];


    if ($connection) {
		$successful = true;
		
		for($i = 0; $i < $billIDSize; $i++){
			$billIDKey = "billID".$i;
			$billID = $_POST[$billIDKey];
			
			$updateSQL = "UPDATE BILL SET
							PAIDDATE = '".$paymentDate."',
							PAYMENT = '".$cardID."'
							WHERE ID = '".$billID."'";
							
			if(mysqli_query($connection, $updateSQL)){
			}else{
				$successful = false;
			}		
		}
			
														
		if($successful){
			
			echo "success";
								
		} else echo "failed updating user, edit profile failed";
		
    } else echo "Database connection failed";
} else echo "All fields are required";