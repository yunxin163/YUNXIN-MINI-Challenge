<?php
/* 获取数据库传感器数据
 * 封装为json给
 *  */

header("content-type:text/html;charset=utf8");
include_once 'conn.php';
$query = "select * from sensor where userid='162210702113';";

mysqli_query($link,'set names utf8');
$result = mysqli_query($link, $query);
// if ($result==true) {
//     echo "查询成功!";
// }else{
//     echo "查询失败!";
// }

$temperature = null;
$humidity = null;
$smoke = null;

//获取查询的数据
if (mysqli_num_rows($result) > 0) {
    while($row = mysqli_fetch_assoc($result)) {
        $temperature = $row["temperature"];
        $humidity = $row["humidity"];
        $smoke = $row["smoke"];
    }
} else {
    echo "0 结果";
}
//封装为array
$arr = array('temperature' => $temperature,
    'humidity' => $humidity,
    'smoke' => $smoke,);
//关闭连接
mysqli_close($link);
//返回json
echo json_encode($arr,JSON_UNESCAPED_UNICODE);

?>