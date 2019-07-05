<?php
//连接数据库

$link = mysqli_connect("localhost","root","crb","myDB") or die("连接数据库服务器失败！".mysqli_errno());
mysqli_query($link,"set names 'utf8'");
?>