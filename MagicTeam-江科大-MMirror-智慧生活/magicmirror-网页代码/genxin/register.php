<?php
/*POST
 *1.获取app通过post请求发送的用户名（userid）和密码（password）
 *2.连接数据库，在用户表中增加新用户
 *3.返回状态-json方式
 */
header("content-type:text/html;charset=utf8");
//客户端post过来的用户名，密码
$userid = $_GET['userid'];
@$username = $_GET['username'];
$password = $_GET['password'];
$back = null;
//用户登录，服务器进行的处理
$link = mysqli_connect("localhost","root","crb","myDB");
$sql = mysqli_query($link,"SELECT * FROM user WHERE userid ='$userid'");
$result = mysqli_fetch_assoc($sql);
if(!empty($result)){
    //存在该用户
    if($password == $result['password']){
        //用户名密码匹配正确
        //mysqli_query($link,"UPDATE user SET status='1' WHERE id = $result[id]");
        $back['status'] = "1";
        $back['info'] = "登陆成功！";
    }else{
        //密码错误
        $back['status']="-2";
        $back['info']="密码错误！";
    }
}else{
    //不存在该用户，自动创建新用户
    $result=mysqli_query($link,"INSERT INTO `user` (`userid`, `username`, `password`, `logstatus`) VALUES ('$userid', '$username', '$password', '1')");
    if($result){
        $back['status']="1";
        $back['info']="已创建新用户！";
    }else{
        $back['status']="-1";
        $back['info']="用户不存在且无法创建该用户！";
    }
}
mysqli_close($link);
echo(json_encode($back,JSON_UNESCAPED_UNICODE));
?> 