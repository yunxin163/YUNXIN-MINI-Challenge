<?php
header("content-type:text/html;charset=utf-8"); 
$servername = "localhost";
$username = "root";
$password = "crb";
$dbname = "myDB";
session_start();
// 创建连接
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("连接失败: " . $conn->connect_error);
} 
if(!empty($_GET['username'])){
	echo $_GET['username'];
	$username= $_GET['username'];
	$sql= "DELETE FROM mima WHERE username = '$username'";
    $result= mysqli_query($conn,$sql);
}
header("Location:dongtailiebiao.php");
?>