<?php
if( !empty($_POST['name']) && 
	!empty($_POST['password']) && 
	!empty($_POST['retypePassword']) && 
	!empty($_POST['email']) && 
	!empty($_POST['street']) && 
	!empty($_POST['blockNo']) && 
	!empty($_POST['unitNo']) && 
	!empty($_POST['postalCode']) && 
	!empty($_POST['phoneNo']) && 
	!empty($_POST['houseType']) && 
	!empty($_POST['householdSize'])){
		
    $connection = mysqli_connect("localhost", "root", "", "fyp");
	$name = $_POST['name'];
    $password = $_POST['password'];
	$retypePassword = $_POST['retypePassword'];
	$email = $_POST['email'];
    $street = $_POST['street'];
	$blockNo = $_POST['blockNo'];
    $unitNo = $_POST['unitNo'];
	$postalCode = $_POST['postalCode'];
    $phoneNo = $_POST['phoneNo'];
    $houseType = $_POST['houseType'];
	$householdSize = $_POST['householdSize'];
	
	$userRole = null;
	$homeownerUserID = null;
	$verificationCode = null;
	
    if ($connection) {
		
		if($password == $retypePassword){
		
			$password = password_hash($_POST['password'], PASSWORD_DEFAULT);
			
					//find homeowner roles ID
			$getRoleSQL = "SELECT ID FROM ROLE WHERE NAME = 'homeowner'";
			$roleSQL = mysqli_query($connection, $getRoleSQL);
			$userRole = mysqli_fetch_row($roleSQL)[0];
			
			//Create the user in the db. Status pending until email verified
			$createUserSQL = "INSERT INTO USERS (NAME, NUMBER, EMAIL, PASSWORD, TYPE, STATUS)
								VALUES('".$name."',
										'".$phoneNo."',
										'".$email."',
										'".$password."',
										'".$userRole."',
										'PENDING')";
		
			if(mysqli_query($connection, $createUserSQL)){
				
				//Get the user ID to create the homeowner, and generate a verfication code for homeowner
				$getUserIDSQL = "SELECT MAX(ID) FROM USERS";
				$homeownerUserID = mysqli_fetch_row(mysqli_query($connection, $getUserIDSQL))[0];
				$verificationCode = rand(100000,999999);
				
				//Create the homeowner in the db. 
				$createHomeownerSQL = "INSERT INTO HOMEOWNER (ID, STREET, BLOCKNO, UNITNO, POSTALCODE, HOUSETYPE, NOOFPEOPLE, CODE)
										VALUES('".$homeownerUserID."',
												'".$street."',
												'".$blockNo."',
												'".$unitNo."',
												'".$postalCode."',
												'".$houseType."',
												'".$householdSize."',
												'".$verificationCode."')";
					
				if(mysqli_query($connection, $createHomeownerSQL)){
					echo "success";
				} else echo "failed creating homeowner, registration failed";
					
			} else echo "failed creating user, registration failed";		
			
		} else echo "re-enter passwords do not match";
		
    } else echo "Database connection failed";
} else echo "All fields are required";