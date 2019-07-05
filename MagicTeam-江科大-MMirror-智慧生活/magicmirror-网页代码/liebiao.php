<?php
    //������Ҫ���ӵ����ݿ⣬localhost�Ǳ��ط�������rootΪ���ݿ���˺ţ��ҵ�����Ϊ0�����ǿ�
    $con = mysqli_connect("localhost","root","crb");
    //�����ַ��������ַ�������Ϊutf8 �ĸ�ʽ�����Ǵ���������Ķ�ʶ���
    mysqli_query("SET NAMES 'utf8'");
    mysqli_query("SET CHARACTER SET utf8");
    if(!$con){
        die(mysqli_error());
    }
    //�������ݿ�test
    mysqli_select_db("myDB",$con);
?>
<!DOCTYPE html>
<html>
<head>
    <title>���ݿ���ʾ</title>
</head>
<body>
//�Ա�����ʽ������ʾ
<table style='text-align:left;' border='1'>
         <tr><th>id</th><th>����</th><th>����</th></tr>
    <?php
    //����conn.php�ļ�
        
        //��ѯ���ݱ��е�����
         $sql = mysqli_query("select * from mima");
         $datarow = mysqli_num_rows($sql); //����
            //ѭ�����������ݱ��е�����
            for($i=0;$i<$datarow;$i++){
                $sql_arr = mysqli_fetch_assoc($sql);
                $id = $sql_arr['id'];
                $name = $sql_arr['username'];
                $age = $sql_arr['password'];
               
                echo "<tr><td>$id</td><td>$name</td><td>$age</td></tr>";
            }
    ?>
</table>
</body>
</html>