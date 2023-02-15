<?php include_once 'config.php';
if (!empty($_POST['userID']) &&
	!empty($_POST['ticketID'])) 
{
	$connection = getDB();
    $userID = $_POST['userID'];
    $ticketID = $_POST['ticketID'];
    $result = array();
	$chatsArr = array();
	
    if ($connection) {
		$chatsSQL = "SELECT A.* , U.NAME FROM CHAT A 
					JOIN TICKET B ON A.TICKET=B.ID 
					JOIN USERS U ON A.SENDER=U.ID 
					WHERE B.HOMEOWNER='".$userID."' 
					AND B.ID='".$ticketID."'
					ORDER BY A.CREATEDATE ASC";
		$chatsResult = mysqli_query($connection, $chatsSQL);	
		if (mysqli_num_rows($chatsResult) != 0) {
			$counter = 0;
			while($chatsRow = mysqli_fetch_array($chatsResult, MYSQLI_ASSOC)){
				$date = $chatsRow["DATE"];
				$ticket = $chatsRow["TICKET"];
				$sender = $chatsRow["SENDER"];
				$text = $chatsRow["TEXT"];
				$name = $chatsRow["NAME"];
				
				$arr = array("date" => $date, 
									"ticket" => $ticket, 
									"name" => $name, 
									"text" => $text, 
									);
				$counter++;				
				$chatsArr[$counter] = $arr;
			}
			$result = array("status" => "success", "message" => "Fetch data successful");
			$result["chats"] = $chatsArr;
			
		} else $result = array("status" => "failed", "message" => "Failed to ticket data");					
    } else $result = array("status" => "failed", "message" => "Database connection failed");
} else $result = array("status" => "failed", "message" => "All fields are required");
echo json_encode($result, JSON_PRETTY_PRINT);
