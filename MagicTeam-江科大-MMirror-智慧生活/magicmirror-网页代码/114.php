<?php
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
if (isset($_POST['username']) && isset($_POST['shebei'])
    && !empty($_POST['username']) && !empty($_POST['shebei'])
) {
	$username = $_POST['username'];
    $shebei = $_POST['shebei'];
	echo $username;
	$_SESSION['views']=$username;
	/*$sql_select="SELECT username FROM mima WHERE username = '$username'";
	$ret = mysqli_query($conn,$sql_select);
    $row = mysqli_fetch_array($ret);
	if($username == $row['username']) {
            //用户名已存在，显示提示信息
            echo "用户名已存在";
       }else{ }*/
	$sql_insert = "UPDATE mima set shebei = '$shebei' WHERE username = '$username'";
            //执行SQL语句
    mysqli_query($conn,$sql_insert);
	echo "sdsds";
   
       
}
echo "sdsds";
mysqli_close($conn);

?>`