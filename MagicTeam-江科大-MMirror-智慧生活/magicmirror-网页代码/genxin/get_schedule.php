<?php 
/* 获取课程表接口信息
 * 解析
 * 重新封装为json接口
 * */  
function curl_post_https($url,$data){ // 模拟提交数据函数
    $curl = curl_init(); // 启动一个CURL会话
    curl_setopt($curl, CURLOPT_URL, $url); // 要访问的地址
    curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, 0); // 对认证证书来源的检查
    //curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, 1); // 从证书中检查SSL加密算法是否存在
    curl_setopt($curl, CURLOPT_USERAGENT, $_SERVER['HTTP_USER_AGENT']); // 模拟用户使用的浏览器
    curl_setopt($curl, CURLOPT_FOLLOWLOCATION, 1); // 使用自动跳转
    curl_setopt($curl, CURLOPT_AUTOREFERER, 1); // 自动设置Referer
    curl_setopt($curl, CURLOPT_POST, 1); // 发送一个常规的Post请求
    curl_setopt($curl, CURLOPT_POSTFIELDS, $data); // Post提交的数据包
    curl_setopt($curl, CURLOPT_TIMEOUT, 30); // 设置超时限制防止死循环
    curl_setopt($curl, CURLOPT_HEADER, 0); // 显示返回的Header区域内容
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1); // 获取的信息以文件流的形式返回
    $tmpInfo = curl_exec($curl); // 执行操作
    if (curl_errno($curl)) {
        echo 'Errno'.curl_error($curl);//捕抓异常
    }
    curl_close($curl); // 关闭CURL会话
    $info = json_decode($tmpInfo,true);//对json格式解码
    return $info; // 返回数据
}

function get_json(){
    $url = 'https://guohe3.com/api/student/getSchoolTimetable?';
    $post_data['username'] = '162210702113';
    $post_data['password'] = 'wana5gsm';
    $post_data['semester'] = '2018-2019-2';
    
    $res = curl_post_https($url, $post_data);
    
    //print_r($res);//打印数据
    return $res;
}

//获取课程表json数据
$json_data = get_json();

//定义常量
define("FIRST",$json_data['info']['0']);//第一节课-常量
define("SECOND",$json_data['info']['1']);//第二节课-常量
define("THIRD",$json_data['info']['2']);//第三节课-常量
define("FOURTH",$json_data['info']['3']);//第四节课-常量
define("FIFTH",$json_data['info']['4']);//第五节课-常量
define("WEEK", get_week());
define("NOCLASS","这一节没课哦~");
//定义变量
$arr = null;
$first = null;
$second = null;
$third = null;
$fourth = null;
$fifth = null;

//处理课程表接口的json数据，并重新封装所需json数据
if (date("l")=="Monday"){
    $arr = toArrary('monday');
    
}elseif (date("l")=="Tuesday"){
    $arr = toArrary('tuesday');
    
}elseif (date("l")=="Wednesday"){
    $arr = toArrary('wednesday');
    
}elseif (date("l")=="Thursday"){
    $arr = toArrary('thursday');
    
}elseif (date("l")=="Friday"){
    $arr = toArrary('friday');
    
}elseif (date("l")=="Saturday"){
    $arr = toArrary('saturday');
    
}elseif (date("l")=="Sunday"){
    $arr = toArrary('sunday');
}


//封装为array，返回array
function toArrary($whatday)
{
    if($whatday=="saturday"||$whatday=="sunday"){
        $first = NOCLASS;
        $second = NOCLASS;
        $third = NOCLASS;
        $fourth = NOCLASS;
        $fifth = NOCLASS;
    }else{
        @$class = FIRST[$whatday];
        $first = pretreatment($class);
        @$class = SECOND[$whatday];
        $second = pretreatment($class);
        @$class = THIRD[$whatday];
        $third = pretreatment($class);
        @$class = FOURTH[$whatday];
        $fourth = pretreatment($class);
        @$class = FIFTH[$whatday];
        $fifth = pretreatment($class);
        //echo FIFTH[$whatday];
        //echo $fifth;
    }
    //封装
    $array = array('first'=>$first,
        'second'=>$second,
        'third'=>$third,
        'fourth'=>$fourth,
        'fifth'=>$fifth
    );
    return $array;
}

//预处理数据
function pretreatment($class)
{
    if($class==null)
    {
        $class = NOCLASS;
    }else {
        $class_all = explode("@---------------------",$class);
        for($i=0;$i<count($class_all);$i++){
            $class=checkClass($class_all[$i]);
            if($class!=NOCLASS) break;
        }     
    }
    return $class;
}
function checkClass($class){
    if($class==null){
        $class = NOCLASS;
    }else{
        $class_arr = explode("@", $class);
        $class_arr[3] = rtrim($class_arr[3], "(周)");
        $num = explode("-",$class_arr[3]);
        $num_begin = (int)$num[0];@$num_end = (int)$num[1];
        $class= NOCLASS;//初始化
        if($num_end==null)
        {
            if(WEEK==$num_begin)
            {
                $class = $class_arr[1];
            }
        }else {
            for($i=$num_begin;$i<=$num_end;$i++)
            {
                if(WEEK==$i)
                {
                    $class = $class_arr[1];break;
                }
            }
        }
    }
    return $class;
}
//求当前是开学第几周，返回周数
function get_week()
{
    $today = date_create("today");//创建日期-今天
    $startday = date_create("2019-2-25");//创建日期-开学日期
    $days = date_diff($startday,$today)->format("%a");//求开学日期距今天的天数
    $week = (int)($days/7)+1;//当前是开学第几周
    return $week;
}


//返回json数据
header("content-type:text/html;charset=utf8");
echo json_encode($arr,JSON_UNESCAPED_UNICODE);
?>

