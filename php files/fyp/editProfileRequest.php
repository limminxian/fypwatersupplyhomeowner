<?php
if( !empty($_POST['userID']) && 
	!empty($_POST['name']) && 
	!empty($_POST['email']) && 
	!empty($_POST['street']) && 
	!empty($_POST['blockNo']) && 
	!empty($_POST['unitNo']) && 
	!empty($_POST['postalCode']) && 
	!empty($_POST['phoneNo']) && 
	!empty($_POST['houseType']) && 
	!empty($_POST['householdSize'])){
		
    $connection = mysqli_connect("localhost", "root", "", "fyp");
	$userID = $_POST['userID'];
	$name = $_POST['name'];
	$email = $_POST['email'];
    $street = $_POST['street'];
	$blockNo = $_POST['blockNo'];
    $unitNo = $_POST['unitNo'];
	$postalCode = $_POST['postalCode'];
    $phoneNo = $_POST['phoneNo'];
    $houseType = $_POST['houseType'];
	$householdSize = $_POST['householdSize'];
	
    if ($connection) {
			
		//find homeowner roles ID
		$updateUserSQL = "UPDATE USERS SET
							NAME = '".$name."',
							NUMBER = '".$phoneNo."',
							EMAIL = '".$email."'
							WHERE ID = '".$userID."'";
							
		$updateHomeownerSQL = "UPDATE HOMEOWNER SET
								STREET = '".$street."',
								BLOCKNO = '".$blockNo."',
								UNITNO = '".$unitNo."',
								POSTALCODE = '".$postalCode."',
								HOUSETYPE = '".$houseType."',
								NOOFPEOPLE = '".$householdSize."'
								WHERE ID = '".$userID."'";
							
		if(mysqli_query($connection, $updateUserSQL)){
			
			if(mysqli_query($connection, $updateHomeownerSQL)){
			
				echo "success";
				
			} else echo "failed updating homeowner, edit profile failed";
				
		} else echo "failed updating user, edit profile failed";
		
    } else echo "Database connection failed";
} else echo "All fields are required";