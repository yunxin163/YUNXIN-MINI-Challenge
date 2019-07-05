<?php
header("Content-type:text/html;charset=utf-8");//字符编码设置

include_once("conn.php");

$userid = $_GET['userid'];

// 检测连接
$sql = "select * from memo where userid='$userid'";
$result = mysqli_query($link,$sql);
if (!$result) {
    printf("Error: %s\n", mysqli_error($link));
    exit();
}

// $arr = array();

// while ($rows=mysqli_fetch_array($result)){
//     $count=count($rows);//不能在循环语句中，由于每次删除 row数组长度都减小
//     for($i=0;$i<$count;$i++){
//         unset($rows[$i]);//删除冗余数据
//     }
//     array_push($arr,$rows);
// }
// $json=json_decode($arr);
// print_r($arr);
// echo json_encode($json,JSON_UNESCAPED_UNICODE);

$i=0;
$row=mysqli_fetch_assoc($result);
do{
    $rows[$i]=$row;
    $i++;
}
while ($row=mysqli_fetch_assoc($result));

echo json_encode($rows,JSON_UNESCAPED_UNICODE);

mysqli_close($link);
?> 