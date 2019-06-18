<?php
require_once 'conn.php';
session_start();
// Check connection
if ($link->connect_error) {
    die("数据库连接失败: " . $link->connect_error);
}
if (isset($_POST['username']) && isset($_POST['password'])
    && !empty($_POST['username']) && !empty($_POST['password']))
{
    $username = $_POST['username'];
    $password = $_POST['password'];
	$_SESSION['views']=$username;
	$sql = "SELECT * FROM admin WHERE (adminid='$username') AND (password='$password')";
	$result = $link->query($sql);
	if ($result->num_rows > 0) {
		while($row = $result->fetch_assoc()){
			header("location: genxin/dongtailiebiaoadmin.php");
		}	
	}
	else {
	    echo "<script>alert('没有该管理员或密码错误！');</script>";
	}
}
