<?php
$servername = "localhost";
$username = "root";
$password = "crb";
$dbname = "myDB";
session_start();
// ��������
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("����ʧ��: " . $conn->connect_error);
} 
 

if (isset($_POST['username']) && isset($_POST['password'])
    && !empty($_POST['username']) && !empty($_POST['password'])
) {
	echo $_POST['username'];
	echo "\r\n";
	
	echo $_POST['password'];
    $username = $_POST['username'];
    $password = $_POST['password'];
	$_SESSION['views']=$username;
	echo "wcaonidaye";

	echo $_SESSION['views'];
	$sql = "SELECT * FROM admin WHERE (adminid='$username') AND (password='$password')";
	echo $sql;
	$result = $conn->query($sql);
	if ($result->num_rows > 0) {
		while($row = $result->fetch_assoc()){
			echo "id: " . $row["adminid"]. " - Name: " . $row["password"].  "<br>";
			header("location: genxin/dongtailiebiaoadmin.php");
			echo "<a href='4get4.php'>4<a>";

			}
			
	}
	else {
		echo "666666";
	}