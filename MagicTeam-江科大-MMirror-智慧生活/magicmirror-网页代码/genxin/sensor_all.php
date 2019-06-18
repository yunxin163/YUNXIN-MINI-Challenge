<?php
/* 获取数据库所有传感器数据
 * 封装为json
 *  */

header("content-type:text/html;charset=utf8");
include_once 'conn.php';
$query = "select * from sensor where userid='162210702113';";

mysqli_query($link,'set names utf8');
$result = mysqli_query($link, $query);

$arr_all = array();
//获取查询的数据
if (mysqli_num_rows($result) > 0) {
    while($row = mysqli_fetch_assoc($result)) {
        $arr_all[]=$row;
    }
} else {
    echo "0 结果";
}
//关闭连接
mysqli_close($link);
//返回json
echo json_encode($arr_all,JSON_UNESCAPED_UNICODE);
?>