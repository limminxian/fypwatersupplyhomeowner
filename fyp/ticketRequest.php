<?php
if (!empty($_POST['ticketID'])
	) {
	$connection = mysqli_connect("us-cdbr-east-06.cleardb.net", "bbd12ae4b2fcc3", "df9ea7aa", "heroku_80d6ea926f679b3");
	$ticketID = $_POST['ticketID'];
    $result = array();
	$description = null;
	$name = null;
	$id = null;
    if ($connection) {
		
		$SQL = "SELECT A.*, B.NAME FROM TICKET A JOIN TICKETTYPE B ON A.TYPE = B.ID WHERE A.ID = '".$ticketID."'";
		$Result = mysqli_query($connection, $SQL);
		if (mysqli_num_rows($Result) != 0){
			$Row = mysqli_fetch_assoc($Result);
			$id = $Row["ID"];
			$description = $Row["DESCRIPTION"];
			$name = $Row["NAME"];
			$result = array("status" => "success", "message" => "Fetch data successful", 
						"id" => $id, "description" => $description, "name" => $name);
		} else $result = array("status" => "failed", "message" => "Failed to fetch ticket data");		
		
		
			
    } else $result = array("status" => "failed", "message" => "Database connection failed");
} else $result = array("status" => "failed", "message" => "All fields are required");
echo json_encode($result, JSON_PRETTY_PRINT);
