<?php
/* GET
 * 请求参数id,userid
 * 删除数据库中备忘录的记录*/
header("content-type:text/html;charset=utf-8");
//连接数据库
include_once("conn.php");

$back = null;

$query = "DELETE FROM memo WHERE id=$_GET[id] AND userid=$_GET[userid]";

$result = mysqli_query($link, $query);
if ($result) {
    $back="true";
} else {
    $back="false";
    echo "Error: " . $query . "<br>" . mysqli_error($link);
}

mysqli_close($link);
echo(json_encode($back,JSON_UNESCAPED_UNICODE));
?>