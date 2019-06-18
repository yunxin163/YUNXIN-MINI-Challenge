<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>dbselect
</title>
<meta charset="utf-8">
</head>
  
<body>
user��
<table border="1">
<tr>
<td>id</td>
<td>username</td>
<td>password</td>
</tr>
<?php
//php�������ݿ��ָ�����������е�һ��root�����ݿ���û������ڶ���root�����ݿ������
//�������ʧ�ܣ�����ͨ��die����Ϻ�������г���ֻ���������ʧ�ܡ�
$con=mysqli_connect("localhost","root","crb");
if(!$con){
 die("����ʧ�ܣ�");
 }
//Ҫ����test���ݿ�
mysqli_select_db("myDB",$con);
//total������������¼user��¼������
$total;
//Ҫ��test���ݿ��в���select count(*) as total from user��䲢�Ұѽ���ŵ�result������
$result=mysqi_query("select count(*) as username from mima");
//result�����Ǹ����ݣ�$total=$row["total"];�Ѳ�ѯ����е�total�е�ֵ�����php�е�total����
//$row=mysql_fetch_array($result)�ܹ��ѵ�ǰ�е�ֵ�����row���飬�����α�����һ�У��α겢����Ҫ��ʼ�����Զ����
while($row=mysqli_fetch_array($result)){
 $total=$row["total"];
}
  
//���������Ĺ���������Ĺ������
$result=mysqli_query("select * from mima");
while($row=mysqli_fetch_array($result)){
 echo "<tr>";
 echo "<td>${row["id"]}</td>";
 echo "<td>${row["username"]}</td>";
 echo "<td>${row["password"]}</td>";
 echo "</tr>";
}
//��ѯ��ϣ��ǵ����ߴ���
mysqli_close($con);
  
?>
</table>
<br />
  
<!--������������������׸����-->
�������ݣ�
<form action="dbinsert.php" method="get">
username:<input type="text" name="username" />
password:<input type="text" name="password" />
<input type="submit" value="go!" />
</form>
  
�޸����ݣ�
<form action="dbupdate.php" method="get">
<select id="userid" name="userid"></select>
<script>
//����php��javascript�������֣��������������php��$total�����������javascript��var total
var total=<?php echo $total; ?>;
var i=1;
for(i=1;i<total+1;i++){
 //javascript���ӽڵ����
 var selectnode=document.createElement("option");
 selectnode.value=i;
 selectnode.innerHTML=i;
 document.getElementById("userid").appendChild(selectnode);
}
</script>
<select name="rowname">
<option value="username">username</option>
<option value="password">password</option>
</select>
<input type="text" name="rowtext" />
<input type="submit" value="go!" />
</form>
  
</body>
</html>