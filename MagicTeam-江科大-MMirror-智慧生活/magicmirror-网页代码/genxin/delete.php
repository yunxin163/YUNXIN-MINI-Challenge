<?php
header("content-type:text/html;charset=utf-8"); 
include_once 'conn.php';
session_start();

if(!empty($_GET['datetime'])){
	$message= $_GET['datetime'];
	$sql= "DELETE FROM memo WHERE datetime = '$datetime'";
    $result= mysqli_query($link,$sql);
    if ($result) {
        echo "删除记录成功！";
    }else {
        echo "删除记录失败！";
    }
}
header("Location:dongtailiebiao2.php");
?>