<?php
if (!empty($_POST['userID'])
) 
{
	$connection = mysqli_connect("us-cdbr-east-06.cleardb.net", "bc292174f8cae7", "68916e25", "heroku_a43ceec7a5c075b");
	$ticketArr = array();
	$result = array();

	if ($connection) {	
	
		$serviceTypeSQL = "SELECT S.NAME FROM USERS U JOIN SERVICETYPE S ON U.ID = S.CREATEDBY JOIN ROLE R ON U.TYPE = R.ID WHERE R.NAME = 'superadmin'";
		$serviceTypeResult = mysqli_query($connection, $serviceTypeSQL);	
		if (mysqli_num_rows($serviceTypeResult) != 0) {
			while($serviceTypeRow = mysqli_fetch_array($serviceTypeResult, MYSQLI_ASSOC)){
				$serviceType = $serviceTypeRow['NAME'];
				array_push($ticketArr, $serviceType);
			}
			$result = array("status" => "success", "message" => "Fetch data successful");
		} else echo $result = array("status" => "failed", "message" => "no default service type found");
	
	
		$serviceTypeSQL = "SELECT S.NAME FROM HOMEOWNER H JOIN COMPANY C ON H.SUBSCRIBE = C.ID JOIN SERVICETYPE S ON C.ADMIN = S.CREATEDBY WHERE H.ID = '".$userID."'";
		$serviceTypeResult = mysqli_query($connection, $serviceTypeSQL);	
		if (mysqli_num_rows($serviceTypeResult) != 0) {
			while($serviceTypeRow = mysqli_fetch_array($serviceTypeResult, MYSQLI_ASSOC)){
				$serviceType = $serviceTypeRow['NAME'];
				array_push($ticketArr, $serviceType);
			}
			$result = array("status" => "success", "message" => "Fetch data successful");
			$result["serviceTypes"] = $ticketArr;
		} else echo $result = array("status" => "failed", "message" => "no company created service type found");
		
	} else echo $result = array("status" => "failed", "message" => "Database connection failed");
} else $result = array("status" => "failed", "message" => "All fields are required");
echo json_encode($result, JSON_PRETTY_PRINT);