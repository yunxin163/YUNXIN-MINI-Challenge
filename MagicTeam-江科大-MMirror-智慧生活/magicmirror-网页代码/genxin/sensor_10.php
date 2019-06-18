<?php
/* 获取数据库所有传感器数据
 * 封装为json
 *  */

header("content-type:text/html;charset=utf8");
include_once 'conn.php';
session_start();
$userid=null;
$query=null;
$arr_10 = array();

if (!empty($_SESSION["views"])) {
    $userid=$_SESSION["views"];
    $query = "SELECT * FROM `sensor` where userid=$userid ORDER BY reg_date DESC LIMIT 10";
    mysqli_query($link,'set names utf8');
    $result = mysqli_query($link, $query);
    //获取查询的数据
    if (mysqli_num_rows($result) > 0) {
        while($row = mysqli_fetch_assoc($result)) {
            $arr_10[]=$row;
        }
    } else {
        echo "0 结果";
    }
}else {
    echo "error:没有该学号.";
}

//关闭连接
mysqli_close($link);
//返回json
echo json_encode($arr_10,JSON_UNESCAPED_UNICODE);
?>