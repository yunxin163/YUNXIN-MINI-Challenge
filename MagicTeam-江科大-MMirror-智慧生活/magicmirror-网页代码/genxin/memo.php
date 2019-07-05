<?php
/* 接口,返回json数据,供魔镜端调用
 * 数据库memo表的数据
 * */
header("content-type:text/html;charset=utf8");

include_once("conn.php");

$query = "select * from memo where userid='162210702117';";

$result = mysqli_query($link, $query);

$type = null;
$title = null;
$content = null;
$datetime =null;
$remindtime = null;

//获取查询的数据
if (mysqli_num_rows($result) > 0) {
    while($row = mysqli_fetch_assoc($result)) {
        $type = $row["type"];
        $title = $row["title"];
        $content = $row["content"];
        $datetime = $row["datetime"];
        $remindtime = $row["remindtime"];
    }
} else {
    echo "0 结果";
}
//封装为array
$arr = array('type' => $type,
            'title' => $title,
            'content' => $content,
            'datetime' => $datetime,
            'remindtime' => $remindtime);
//关闭连接
mysqli_close($link);
//返回json
echo json_encode($arr,JSON_UNESCAPED_UNICODE);
?>