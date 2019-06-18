<?php
//读取本地json文件
$json=file_get_contents("schedule.json");
$json_data=json_decode($json,true);

//定义常量、变量
define("FIRST",$json_data['info']['0']);//第一节课-常量
define("SECOND",$json_data['info']['1']);//第二节课-常量
define("THIRD",$json_data['info']['2']);//第三节课-常量
define("FOURTH",$json_data['info']['3']);//第四节课-常量
define("FIFTH",$json_data['info']['4']);//第五节课-常量
define("WEEK", get_week());
define("NOCLASS","这一节没课哦~");
$arr = null;

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