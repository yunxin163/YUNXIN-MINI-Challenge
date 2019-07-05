<?php
$servername = "localhost";
$username = "root";
$password = "crb";
$dbname = "myDB";

// 创建连接
$conn = new mysqli($servername, $username, $password, $dbname);
// 检测连接
if ($conn->connect_error) {
    die("连接失败: " . $conn->connect_error);
} 
$result = $conn->query("set names 'gbk'");
// 使用 sql 创建数据表
$sql = "select * from data";
$result = $conn->query($sql);
	if ($result->num_rows > 0) {
		while($row = $result->fetch_assoc()){
			echo "id: " . $row["username"]. " - Name: " . $row["time"]. " - Name: " . $row["zhuti"]. " - Name: " . $row["message"].  "<br>";
			
			echo "<a href='4get4.php'>4<a>";

			}
			
	}


$conn->close();
?>