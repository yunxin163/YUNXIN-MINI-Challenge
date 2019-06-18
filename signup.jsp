<%@ page language="java" import="java.util.*" pageEncoding="utf8"%>
<! DOCTYPE html>
<html>
<head>
<title>注册页面</title>
<link rel="stylesheet" type="text/css" href="css/signup.css">
</head>
<body>
<div class="div1">
</div>
<div class="div2">
<span>欢迎注册</span>
</div>
<div class="div3">
已有账号 <a href="http://localhost:8080/Prj14/login.jsp" >直接登陆</a>
</div>
<div class="div4">
<form action="/Prj14/signup.action" method="post">
<input type="text" placeholder="输入账号" id="text1" maxlength="10" name="account"><br>
<span style="font-size:10px;padding-left;margin-right:160px">最多输入10个字符</span>
<br><br>
<input type="text" placeholder="输入密码" id="text2" maxlength="10" name="password">
<br>
<span style="font-size:10px;padding-left;margin-right:160px">最多输入10个字符</span>
<br><br>
<input type="checkbox" style="font-size:10px" onclick="if (this.checked) {able()} else {disable()}">
<a href="zhucexieyi.jsp"><span style="margin-right:50px">同意注册协议</span></a>
<input type="submit" id="check"  disabled="true" value="注册" style="width:100px;height:30px">
</form>
</div>
<div class="div5">作者：研职</div>
</body>
<script src="js/signupjs.js"></script>
</html>