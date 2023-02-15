<?php include config.php;
    $conn = mysqli_connect("us-cdbr-east-06.cleardb.net", "bbd12ae4b2fcc3", "df9ea7aa", "heroku_80d6ea926f679b3");
    if (mysqli_connect_errno()) {
        echo "Failed to connect to MySQL: " . mysqli_connect_error();
        die();
    }

    //creating a query
    //$stmt = $conn->prepare("SELECT ID, HOMEOWNERNAME, SERVICETYPE, DESCRIPTION, ADDRESS, STATUS FROM tasktest;");
    $stmt = $conn->prepare("SELECT * FROM COMPANY");
    // table - field
    // homeowner - name (from user table)
    // ticket - type
    // ticket - description
    // ticket - status
    // where clause = area (from homeowner table)
    



    //executing the query 
    $stmt->execute();
    
    //binding results to the query 
    $stmt->bind_result($ID, $NAME, $STREET, $POSTALCODE, $DESCRIPTION, $ADMIN, $NOOFSTAR, $NOOFRATE);
    
    $tasks = array(); 
    
    //traversing through all the result 
    while($stmt->fetch()){
        $task = array();
        $task['ID'] = $ID; 
		$task['NAME'] = $NAME;
		$task['STREET'] = $STREET;
		$task['POSTALCODE'] = $POSTALCODE;
		$task['DESCRIPTION'] = $DESCRIPTION;
		$task['ADMIN'] = $ADMIN;
		$task['NOOFSTAR'] = $NOOFSTAR;
		$task['NOOFRATE'] = $NOOFRATE;		
        array_push($tasks, $task);
    }
    
    //displaying the result in json format 
    echo json_encode($tasks);
?>