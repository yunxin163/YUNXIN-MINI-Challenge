 <%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>登陆界面</title> 
<link rel="stylesheet" type="text/css" href="css/logincss.css">
</head>
<!-- 页面加载时调用pageload()函数 -->
<body onload="pageload()">
<div class="div1">
</div>
<div class="div2">
<br><br><br>
<img src="images/yz.png">
没有账号？<a href="http://localhost:8080/Prj14/signup.jsp" style="color:red">立即注册</a>
<form action="/Prj14/login.action" method="post">
<br><br><br><br><br>
<span style="color:red">账号:</span>
<input type="text" id="text1" placeholder="学号/工号" name="account"><br><br>
<span style="color:red">密码:</span>
<input type="password" id="password1"  placeholder="密码" name="password">
<br>
<div style="margin-right:125px"><input type="checkbox" id="Re" name="Re">记住密码</div>
<br>
<input type="submit"  style="width:100px;height:30px" value="登陆" onclick="check()"><br>
</form>
</div>
<div class="div3">作者：研职</div>         
</body>
<script src="js/loginjs.js"></script>
</html>