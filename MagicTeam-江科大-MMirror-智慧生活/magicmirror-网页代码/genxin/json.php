<?php
header("content-type:text/html;charset=utf-8"); 
$servername = "localhost";
$username = "root";
$password = "crb";
$dbname = "myDB";
$acckey="aQhiPsTqV9jHnnE7";
session_start();
// 创建连接


/*mysql_query("set names 'utf8'");*/

/*$json_strget=json_encode($str_get);


$rid=$_POST['username'];
$distance=$_POST['key'];
$key=$_POST['jifen'];

if(empty($rid)||empty($distance)||empty($key)){
	die("error;empty");}
	else{echo "666666";}
	*/
	$str_get = file_get_contents("php://input");
	if(!empty($str_get)){
		//echo "111";

		$json_strget=json_decode($str_get,true);
		echo "JSON decode:".$json_strget;
		
		$userid=$json_strget['username'];
		$key=$json_strget['key'];
		$jifen=$json_strget['jifen'];





		function icon_to_utf8($s) {

					if(is_array($s)) {
										 foreach($s as $key => $val) {
			  $s[$key] = icon_to_utf8($val);
				 }
				 } /*else {
						  $s = ct2($s);
			  }*/
				 return $s;

}

        function JSON($str){
			$json = json_encode($str);
			return preg_replace("#\\\u([0-9a-f]+)#ie","iconv('UCS-2','UTF-8', pack('H4', '\\1'))",$json);
	}
	
















		$conn = mysqli_connect($servername, $username, $password, $dbname);
	    
		//$sql="select message,time from data where shebei='$key'";
		$sql="select message,time from data where username='1441903116'";
        $result=mysqli_query($conn,$sql);
		if (mysqli_num_rows($result) > 0) {
    // 输出数据
			while($row = mysqli_fetch_assoc($result)) {
				$dangtianriqi = date("Y-m-d",time());
				if($dangtianriqi == $row["time"]){
					//$rr = $row["message"];
					//$re =strval($rr);
					//$ss= preg_replace("#\\\u([0-9a-f]{4})#ie", "iconv('UCS-2BE', 'UTF-8', pack('H4', '\\1'))", $row["message"]);
					
					//echo JSON($row["message"]);
					//$newData=iconv("GB2312","UTF-8//IGNORE",$re);  
					//urldecode(json_encode(urlencode($re)));
					//echo $row["message"];
					//echo json_encode(icon_to_utf8($row["message"]));
					echo json_encode(($row["message"]));}
					//return preg_replace("#\\\u([0-9a-f]{4})#ie", "iconv('UCS-2BE', 'UTF-8', pack('H4', '\\1'))", $row["message"]);
					}
		} else {
				 echo "0 结果";
			}
		mysqli_close($conn);



        //echo $userid;
		//echo $key;
		//echo $jifen;
		header("content-type:text/json");
		$po='祝你今天有个好心情';
        echo json_encode($po);
		//return preg_replace("#\\\u([0-9a-f]{4})#ie", "iconv('UCS-2BE', 'UTF-8', pack('H4', '\\1'))", $po);
/*		$str_return=$str_get;
		$str_return.=" ";
		$str_return.="rid:".$rid." ";
		$str_return.="distance:".$distance." ";
		$str_return.="key:".$key." ";
		writeToFile($str_return);
		
	
            
	else{echo "77777";}
	*/  $conn=new mysqli("localhost","root","crb","myDB");
	    $username='1441903116';
		$sql="update beiwanglu set userid='$key',jifen='$jifen' where username='$username'";
        $result=mysqli_query($conn,$sql);
        
	}

/*
if(!empty($rid)&&!empty($distance)&&!empty($key)){
			if($key==$acckey){
				$place="江苏科技大学";
				$sql = "update recyclingbins set distance='".$distance."',place='".$place."' where rid='".$rid."'";
				mysqli_query($sql,$conn);//执行SQL
				$mark  = mysql_affected_rows();//返回影响行数

				if($mark>0){
					echo "success"."<br/>";
				}else{
					echo "failed"."<br/>";
				}
				die('rid:'.$rid.'<br/>'.'distace:'.$distance.'<br/>'.'数据接收并匹配'.'<br/>');
			}else{
				die('error:rejected');
			}
		}else{
			die('error:order empty');
		}
		
	}else{
		die('error:empty');
	}*/
	
?>