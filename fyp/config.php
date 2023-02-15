<?php 
function getDB(){
	$hostName = "us-cdbr-east-06.cleardb.net";
	$username = "bc292174f8cae7";
	$password = "68916e25";
	$db = "heroku_a43ceec7a5c075b";
	$port = "";
	
	$connection = mysqli_connect($hostName, $username, $password, $db);
	return $connection;
}
