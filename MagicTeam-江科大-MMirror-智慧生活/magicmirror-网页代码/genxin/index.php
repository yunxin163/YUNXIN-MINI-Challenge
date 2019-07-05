<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>留言表单</title>
<link href="css/style1.css" rel='stylesheet' type='text/css' />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="Pink Contact Form ,Login Forms,Sign up Forms,Registration Forms,News latter Forms,Elements"./>

<!--webfonts-->
<!--<link href='http://fonts.useso.com/css?family=Lato:100,300,400,700,100italic,300italic,400italic|Lora:400,700,400italic,700italic' rel='stylesheet' type='text/css'>
<link href='http://fonts.useso.com/css?family=Raleway:400,200,300,500,600,800,700,900' rel='stylesheet' type='text/css'>-->
<!--//webfonts-->

</head>
	<body>
		<div class="zone">
			<h1>备忘记录</h1>
			<div class="login-01" >
				<form action="insertMemo.php"  method="post">
					<ul>
						<li class="first">
							<a href="#" class=" icon user"></a>
							<input type="text" class="text" name="username" value="用户名" onFocus="this.value = '';" onBlur="if (this.value == '') {this.value = '用户名';}" >
							<div class="clear"></div>
						</li>
						<li class="first">
							<a href="#" class=" icon email"></a>
							<div class="radioGroup">
								<br/>
								<input type="date" class="text" name="datetime" value="时间" onFocus="this.value = '';" onBlur="if (this.value == '') {this.value = '时间';}" >
								<div class="clear"></div>
							</div>
						</li>
						<li class="first">
							<a href="#" class=" icon phone"></a>
							<div class="radioGroup">
								<div style="font-size:18px;margin-left:25%;font-family:'Arial Black', Gadget, sans-serif">类型</div>
								<label><input type="radio" class="text" name="type" value="记录心情" checked="checked">记录心情</label>
								<label><input type="radio" class="text" name="type" value="生活经验" >生活经验</label>
								<label><input type="radio" class="text" name="type" value="课堂笔记" >课堂笔记</label>
								<label><input type="radio" class="text" name="type" value="待办事项" >待办事项</label>
								<div class="clear"></div>
							</div>
						</li>
						<li class="first">
							<a href="#" class=" icon phone"></a>
							<input type="text" class="text" name="title" value="标题" onFocus="this.value = '';" onBlur="if (this.value == '') {this.value = '标题';}" >
							<div class="clear"></div>
						</li>
						<li class="second">
						<a href="#" class=" icon msg"></a>
						<textarea name="message" value="内容" onFocus="this.value = '';" onBlur="if (this.value == '') {this.value = '内容';}">内容</textarea>
						<div class="clear"></div>
						</li>
					</ul>
					<input type="submit" onClick="myFunction()" value="Submit" >
					<br/>
					<div class="clear"></div>
				</form>
			</div>
		</div>
		<!--start-copyright-->
		<div class="copy-right">
			<div class="wrap">
			</div>
		</div>
		<!--//end-copyright-->
	</body>
</html>