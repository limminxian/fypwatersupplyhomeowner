<?php include_once 'config.php';
if (!empty($_POST['userID'])) {
	$connection = getDB();
	$userID = $_POST['userID'];
	$waterArr = array();
	$result = array();

	if ($connection) {	
		$waterUsageSQL = "SELECT RECORDDATE, DAY(RECORDDATE) DAY, MONTH(RECORDDATE) MONTH, YEAR(RECORDDATE) YEAR, `WATERUSAGE(L)` FROM WATERUSAGE WHERE HOMEOWNER = '".$userID."'";
		$waterUsageResult = mysqli_query($connection, $waterUsageSQL);	
		if (mysqli_num_rows($waterUsageResult) != 0) {
			while($waterUsageRow = mysqli_fetch_array($waterUsageResult, MYSQLI_ASSOC)){
				$date = $waterUsageRow['RECORDDATE'];
				$day = $waterUsageRow['DAY'];
				$month = $waterUsageRow['MONTH'];
				$year = $waterUsageRow['YEAR'];
				$waterUsage = $waterUsageRow['WATERUSAGE(L)'];
				
				$arr = array("day" => $day, 
					"month" => $month, 
					"year" => $year, 
					"waterUsage" => $waterUsage
				);
				
				$waterArr[$date] = $arr;
			}
			$result = array("status" => "success", "message" => "Fetch data successful");
			$result["waterUsages"] = $waterArr;
			
		} else echo $result = array("status" => "failed", "message" => "Failed to fetch water usage");
		
	}else echo $result = array("status" => "failed", "message" => "Database connection failed");
		
} else $result = array("status" => "failed", "message" => "All fields are required");
echo json_encode($result, JSON_PRETTY_PRINT);