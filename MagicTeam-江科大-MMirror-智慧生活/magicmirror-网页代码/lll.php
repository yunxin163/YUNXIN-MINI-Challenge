<?php
require_once ("conn.php");
session_start();
if ($link->connect_error) {
    die("数据库连接失败: " . $link->connect_error);
} 
if (isset($_POST['username']) && isset($_POST['password'])
    && !empty($_POST['username']) && !empty($_POST['password'])) 
{
    $_SESSION['views'] = $_POST['username'];
    $userid = $_POST['username'];
    $password = $_POST['password'];
	$sql = "SELECT * FROM user WHERE (userid='$userid') AND (password='$password')";
	$result = $link->query($sql);
	if ($result->num_rows > 0) {
		while($row = $result->fetch_assoc()){
			header("location: genxin/dongtailiebiao2.php");
		}
	}
	else {
		echo "<script>alert('没有该用户或密码错误！');window.location=\"http://101.132.169.177/magicmirror/denglu1.html\"</script>";
	}
}
?>