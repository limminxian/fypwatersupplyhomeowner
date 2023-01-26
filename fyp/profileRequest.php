<?php
if (!empty($_POST['userID'])) {
	$connection = mysqli_connect("localhost", "root", "", "fyp");
    $userID = $_POST['userID'];
    $result = array();
	
	$name = null;
    $password = null;
	$email = null;
    $street = null;
	$blockNo = null;
    $unitNo = null;
	$postalCode = null;
    $phoneNo = null;
    $houseType = null;
	$householdSize = null;
	
    if ($connection) {
        $userSQL = "SELECT * FROM USERS WHERE ID = '" . $userID . "'";
        $userResult = mysqli_query($connection, $userSQL);
		
		$homeownerSQL = "SELECT * FROM HOMEOWNER WHERE ID = '".$userID."'";
		$homeownerResult = mysqli_query($connection, $homeownerSQL);
		
		if (mysqli_num_rows($userResult) != 0) {
			$userRow = mysqli_fetch_assoc($userResult);
			
			$name = $userRow['NAME'];
			$phoneNo = $userRow['NUMBER'];
			$email = $userRow['EMAIL'];
			
		} else $result = array("status" => "failed", "message" => "Failed to fetch user data");		
		
		if (mysqli_num_rows($homeownerResult) != 0) {
			$homeownerRow = mysqli_fetch_assoc($homeownerResult);
			
			$street = $homeownerRow['STREET'];
			$blockNo = $homeownerRow['BLOCKNO'];
			$unitNo = $homeownerRow['UNITNO'];
			$postalCode = $homeownerRow['POSTALCODE'];
			$houseType = $homeownerRow['HOUSETYPE'];
			$householdSize = $homeownerRow['NOOFPEOPLE'];
			$result = array("status" => "success", "message" => "Fetch data successful", 
						"name" => $name,
						"phoneNo" => $phoneNo,
						"email" => $email,
						"street" => $street,
						"blockNo" => $blockNo,
						"unitNo" => $unitNo,
						"postalCode" => $postalCode,
						"houseType" => $houseType,
						"householdSize" => $householdSize);
		} else $result = array("status" => "failed", "message" => "Failed to fetch homeowner data");		
		
		
		
    } else $result = array("status" => "failed", "message" => "Database connection failed");
} else $result = array("status" => "failed", "message" => "All fields are required");
echo json_encode($result, JSON_PRETTY_PRINT);
