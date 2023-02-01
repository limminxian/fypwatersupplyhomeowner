<?php
if (!empty($_POST['userID']) &&
	!empty($_POST['ticketID'])) 
{
	$connection = mysqli_connect("us-cdbr-east-06.cleardb.net", "bbd12ae4b2fcc3", "df9ea7aa", "heroku_80d6ea926f679b3");
    $userID = $_POST['userID'];
    $ticketID = $_POST['ticketID'];

    $result = array();
	$chatsArr = array();
	
    if ($connection) {
		$chatsSQL = "SELECT A.* , U.NAME FROM CHAT A JOIN TICKET B ON A.TICKET=B.ID JOIN USERS U ON A.SENDER=U.ID WHERE B.HOMEOWNER='".$userID."' AND B.ID='".$ticketID."';";
		$chatsResult = mysqli_query($connection, $chatsSQL);	
		if (mysqli_num_rows($chatsResult) != 0) {
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
									
				$chatsArr[$sender] = $arr;
			}
			$result = array("status" => "success", "message" => "Fetch data successful");
			$result["chats"] = $chatsArr;
			
		} else $result = array("status" => "failed", "message" => "Failed to ticket data");					
    } else $result = array("status" => "failed", "message" => "Database connection failed");
} else $result = array("status" => "failed", "message" => "All fields are required");
echo json_encode($result, JSON_PRETTY_PRINT);
