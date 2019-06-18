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

if (isset($_POST['username']) && isset($_POST['datetime']) && isset($_POST['title']) && isset($_POST['message'])
    && !empty($_POST['username']) && !empty($_POST['datetime']) && !empty($_POST['title']) && !empty($_POST['message'])
) {
	/*echo $_POST['username'];
	echo "\r\n";
	
	echo $_POST['datetime'];
	echo $_POST['title'];
	echo $_POST['message'];*/
    $username = $_POST['username'];
    $datetime = $_POST['datetime'];
	$title = $_POST['title'];
	$message = $_POST['message'];
	$_SESSION['views']=$username;
	echo $username;
    
	echo $_SESSION['views'];
	$sql = "INSERT INTO memo (userid,title,datetime,content) VALUES('$username','$title','$datetime','$message')";
	
    //执行SQL语句
	mysqli_set_charset ($conn,utf8);
    $result= mysqli_query($conn,$sql);
    header("Location:dongtailiebiao2.php");
	if ($result->num_rows > 0) {
		while($row = $result->fetch_assoc()){
			echo "id: " . $row["zhuti"]. " - time: " . $row["time"]. " - message: " . $row["message"].  "<br>";
		}	
	}
	else {
		echo "false";
	}
}
?>
