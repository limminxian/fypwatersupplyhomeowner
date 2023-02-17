<?php include_once 'config.php';
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

    $connection = getDB();
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
	$area = null; 
	
	$north = array("69","70","71","72","73","75","76");
	$northeast = array("53","54","55","56","57","79","80","82");
	$central = array("01","02","03","04","05","06","07","08","09","10","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","58","59","77","78");
	$east =array("42","43","44","45","46","47","48","49","50","51","52","81");
	$west = array("11","12","13","60","61","62","63","64","65","66","67","68");
	
	// php regex has to start and end with a / slash. 
	$passwordRegex = '/^[^\s]*(?=\S{8,16})(?=\S*[a-z])(?=\S*[A-Z])(?=\S*[\d])(?=\S*[\W])[^\s]*$/';
	$phoneRegex = '/^(8|9)\d{7}$/';
	$postalCodeRegex = '/^[1-9]\d{5}$/';
	
    if ($connection) {
		if(preg_match($passwordRegex, $password)){
			if($password == $retypePassword){
				if(filter_var($email , FILTER_VALIDATE_EMAIL)){
					if(preg_match($phoneRegex, $phoneNo)){
						if(preg_match($postalCodeRegex, $postalCode)){
							$password = password_hash($_POST['password'], PASSWORD_DEFAULT);
							
							//find homeowner roles ID
							$getRoleSQL = "SELECT ID FROM ROLE WHERE NAME = 'homeowner'";
							$roleSQL = mysqli_query($connection, $getRoleSQL);
							$userRole = mysqli_fetch_row($roleSQL)[0];
							
							//get area
							$areaCode = substr($postalCode, 0, 2);
							if(in_array($areaCode, $north)){
								$area = "north";
							}elseif (in_array($areaCode, $northeast)){
								$area = "northeast";
							}elseif (in_array($areaCode, $central)){
								$area = "central";
							}elseif (in_array($areaCode, $east)){
								$area = "east";
							}elseif (in_array($areaCode, $west)){
								$area = "west";
							}
							
							//Create the user in the db. Status pending until email verified
							$createUserSQL = "INSERT INTO USERS (NAME, NUMBER, EMAIL, PASSWORD, TYPE, STATUS)
												VALUES('".$name."',
														'".$phoneNo."',
														'".$email."',
														'".$password."',
														'".$userRole."',
														'PENDING')";
						

							try{
								mysqli_query($connection, $createUserSQL);
								//Get the user ID to create the homeowner, and generate a verfication code for homeowner
								$getUserIDSQL = "SELECT MAX(ID) FROM USERS";
								$homeownerUserID = mysqli_fetch_row(mysqli_query($connection, $getUserIDSQL))[0];
								
								//Create the homeowner in the db. 
								$createHomeownerSQL = "INSERT INTO HOMEOWNER (ID, STREET, BLOCKNO, UNITNO, POSTALCODE, AREA, HOUSETYPE, NOOFPEOPLE)
														VALUES('".$homeownerUserID."',
																'".$street."',
																'".$blockNo."',
																'".$unitNo."',
																'".$postalCode."',
																'".$area."',
																'".$houseType."',
																'".$householdSize."')";
									
								if(mysqli_query($connection, $createHomeownerSQL)){
									echo "success";
								} else echo "failed creating homeowner, registration failed";
							}catch(mysqli_sql_exception $e){
							  echo (mysqli_error($connection));
							}
							// if(mysqli_query($connection, $createUserSQL)){
								
								////Get the user ID to create the homeowner, and generate a verfication code for homeowner
								// $getUserIDSQL = "SELECT MAX(ID) FROM USERS";
								// $homeownerUserID = mysqli_fetch_row(mysqli_query($connection, $getUserIDSQL))[0];
								
								////Create the homeowner in the db. 
								// $createHomeownerSQL = "INSERT INTO HOMEOWNER (ID, STREET, BLOCKNO, UNITNO, POSTALCODE, AREA, HOUSETYPE, NOOFPEOPLE)
														// VALUES('".$homeownerUserID."',
																// '".$street."',
																// '".$blockNo."',
																// '".$unitNo."',
																// '".$postalCode."',
																// '".$area."',
																// '".$houseType."',
																// '".$householdSize."')";
									
								// if(mysqli_query($connection, $createHomeownerSQL)){
									// echo "success";
								// } else echo "failed creating homeowner, registration failed";
									
							// } else echo "failed creating user, registration failed";	
						} else echo "Please insert a valid postal code that includes 6 digits";
					} else echo "Please insert a valid phone number that starts with 6, 8 or 9 and includes 8 digits";
				} else echo "Please insert a valid email address";
			} else echo "re-enter passwords do not match";
		} else echo "Please provide a password that matched rules above";
    } else echo "Database connection failed";
} else echo "All fields are required";