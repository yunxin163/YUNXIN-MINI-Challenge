<?php
/* 获取由树莓派post的传感器信息并上传数据库
 * 上传数据库*/
header("content-type:text/html;charset=utf-8");
//连接数据库
include_once("conn.php");
$temperature=$_GET["temperature"];
$humidity=$_GET["humidity"];
if(($temperature!="no")&&($humidity!="no")&&($humidity<=100)){
    //$query = "updata sensor set temperature=$_GET[temperature],humidity=$_GET[humidity],smoke='$_GET[smoke]' where id='162210702113'";
    $query = "INSERT INTO `sensor` (`id`, `userid`, `temperature`, `humidity`, `smoke`, `reg_date`, `equipment_id`)
                            VALUES (NULL, '162210702113', '$_GET[temperature]', '$_GET[humidity]', '$_GET[smoke]', CURRENT_TIMESTAMP, '1456');";
    
    if (mysqli_query($link, $query)) {
        echo "传感器数据上传成功";
    }else{
        echo "传感器数据上传失败";
    }
}else {
    echo "传感器数据异常";
}
mysqli_close($link);


?>