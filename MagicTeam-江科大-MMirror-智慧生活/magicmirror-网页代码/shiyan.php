<!DOCTYPE html>
<html>
<head>
	<title>Login</title>
		<meta charset="utf-8">
		
		
</head>
 <body>
<form action="demo_keygen.asp" method="get">
   beifangshijian: <input type="text" name="jiludata"><br>
   时  间: <input type="datetime-local" name="datetime">
 
   <input type="submit">
  </form>
 <?php
 session_start();
 echo $_SESSION['views'];
 ?>
  
 </body>
</html>
