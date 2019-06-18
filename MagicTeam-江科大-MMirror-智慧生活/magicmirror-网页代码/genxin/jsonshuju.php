<?php

header("content-type:text/html;charset=utf-8");

$servername = "localhost";
$user = "root";
$password = "crb";
$dbname = "myDB";

session_start();

	$str_get = file_get_contents("php://input");
	if(!empty($str_get)){
		
		$json_strget=json_decode($str_get,true);
		echo "JSON decode:".$json_strget;
		
		$username=$json_strget['username'];
		$yanwu=$json_strget['yanwu'];
		$jifen=$json_strget['jifen'];

        
		header("content-type:text/json");
		
		$conn=new mysqli($servername,$user,$password,$dbname);
	    $username='1441903116';
		$sql="update cgq set yanwu='$yanwu',jifen='$jifen' where username='$username'";
        $result=mysqli_query($conn,$sql);
		echo "ok";
        
	}


?>