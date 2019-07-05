<?php
header("content-type:text/html;charset=utf-8");
$servername = "localhost";
$username = "root";
$password = "crb";
$dbname = "myDB";
session_start();
// 创建连接
$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("连接失败: " . $conn->connect_error);
} 

$userid=null;
$status=null;
if(!empty($_GET['userid'])){
    echo $_GET['userid'];
    $userid= $_GET['userid'];
    if(!empty($_GET['status'])){
        echo $_GET['status'];
        $status=$_GET['status'];
        $sql= "UPDATA user set logstatus='$status' WHERE userid='$userid'";
        $result= mysqli_query($conn,$sql);
    }else {
        echo "status=null";
    }
}else {
    echo "userid=null";
}


header("Location:dongtailiebiaoadmin.php");



?>