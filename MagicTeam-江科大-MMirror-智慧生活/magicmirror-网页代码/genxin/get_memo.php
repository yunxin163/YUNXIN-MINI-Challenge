<?php
/* GET
 * 获取安卓端备忘录信息
 * 上传数据库*/
header("content-type:text/html;charset=utf-8");
//连接数据库
include_once("conn.php");

$back = null;
$id=null;
$userid=null;
$query = "INSERT INTO `memo` (`userid`, `type`, `title`, `content`, `datetime`, `remindtime`)
                            VALUES ('$_GET[userid]','$_GET[type]','$_GET[title]','$_GET[content]','$_GET[datetime]','$_GET[remindtime]')";

//插入数据
$result = mysqli_query($link, $query);
if ($result) {
    $back="true";
    $query1 = "SELECT id,userid FROM memo WHERE userid='$_GET[userid]' AND type='$_GET[type]' AND title='$_GET[title]' AND content='$_GET[content]' AND datetime='$_GET[datetime]' AND remindtime='$_GET[remindtime]'";
    $result1 = mysqli_query($link, $query1);
    if (mysqli_num_rows($result1) > 0) {
        while($row = mysqli_fetch_assoc($result1)) {
            $id = $row["id"];
            $userid = $row["userid"];
        }
    } else {
        echo "0 结果";
    }
} else {
    $back="false";
    echo "Error: " . $query . "<br>" . mysqli_error($link);
}
$arr = array('status' => $back,
            'id' => $id,
            'userid' => $userid);
mysqli_close($link);
echo(json_encode($arr,JSON_UNESCAPED_UNICODE));
?>