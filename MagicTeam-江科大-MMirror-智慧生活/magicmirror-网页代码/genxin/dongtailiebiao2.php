<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />
	<title>Magic Mirror</title>
	<link rel="shortcut icon" type="image/icon" href="images/logo.png"/>
	<link rel="stylesheet" type="text/css" href="https://cdn.bootcss.com/twitter-bootstrap/4.2.1/css/bootstrap.min.css"/>
	<link rel="stylesheet" href="css/style.css" type="text/css" media="all" />
	<!-- Latest compiled and minified CSS -->
	<link rel="stylesheet" href="https://unpkg.com/bootstrap-table@1.14.2/dist/bootstrap-table.min.css">

	

</head>
<body>
<!-- Header -->
<div id="header">
	<div class="shell">
		<!-- Logo + Top Nav -->
		<div id="top">
			<h1><a href="#">用户表</a></h1>
			<div id="top-navigation">
			    <?php
				session_start();
				
				echo "欢迎来到智能镜 ".$_SESSION['views'];

				?>
				
				<a href="dongtailiebiao2.php">用户系统</a>
			</div>
		</div>
		<!-- End Logo + Top Nav -->
		
		<!-- Main Nav -->
		<div id="navigation">
			<ul>
			    <li><a href="shujuxianshi.php" class="active"><span>用户中心</span></a></li>
			    
			</ul>
		</div>
		<!-- End Main Nav -->
	</div>
</div>
<!-- End Header -->
<div class="copyrights">Collect from <a href="http://www.moobnn.com/" >扩展</a> <a href="http://guantaow.taobao.com" target="_blank">扩展</a></div>
<!-- Container -->
<div id="container">
	<div class="shell">
		
		<!-- Small Nav -->
		<div class="small-nav">
			<a href="#">面板</a>
			<span>&gt;</span>
			最近的备忘
		</div>
		<!-- End Small Nav -->
		
		<!-- Message OK -->		
		<div class="msg msg-ok">
			<p><strong>你的智能镜</strong></p>
			<a href="Untitled1.php" class="close">close</a>
		</div>
		<!-- End Message OK -->		
		
		<!-- Message Error 
		<div class="msg msg-error">
			<p><strong>You must select a file to upload first!</strong></p>
			<a href="http://www.runoob.com/css3/css3-backgrounds.html" class="close">close</a>
		</div>       -->
		<!-- End Message Error -->
		<br />
		<!-- Main -->
		<div id="main">
			<div class="cl">&nbsp;</div>
			
			<!-- Content -->
			<div id="content">
				
				<!-- Box -->
				<div class="box">
					<!-- Box Head -->
					<div class="box-head">
						<h2 class="left">最近的记录</h2>
						<div class="right">
							<label>查找</label>
							<input type="text" class="field small-field" />
							<input type="submit" class="button" value="search" />
						</div>
					</div>
					<!-- End Box Head -->	
                  
					
					<div class="table">
						<table id="result" width="100%" cellspacing="0" cellpadding="0" class="table table-bordered" 
						data-pagination="true" data-side-pagination="client" data-page-size="10">
						    <?php
    							include_once 'conn.php';
    						    $uname = $_SESSION['views'];
    						    $result=mysqli_query($link,"select * from memo where userid = '$uname'");
    						    while($row=mysqli_fetch_array($result)){
                                    echo "<tr class='odd';>";
                                    echo "<td><a href=''>${row["userid"]}</a></td>";
                                    echo "<td>${row["type"]}</td>";
    							    echo "<td>${row["title"]}</td>";
    							    echo "<td>${row["content"]}</td>";
    							    echo "<td>${row["datetime"]}</td>";
    							    echo "<td>${row["remindtime"]}</td>";
    							    echo "<td><a href='delete.php?userid=$row[datetime]'>Delete</a></td>";
                               }
                               mysqli_close($link);
                           ?>
						</table>
					</div>
					<!-- Table -->
					
				</div>
				<!-- End Box -->

				<!--监控图表模块-->
				<div class="box">
					<div class="box-head">
						<h2>监控</h2>
					</div>
					<div id="weather-line" style="width: 725px;height:375px;"></div>
				</div>
				
			</div>
			<!-- End Content -->
			
			<!-- Sidebar -->
			<div id="sidebar">
				
				<!-- Box -->
				<div class="box">
					
					<!-- Box Head -->
					<div class="box-head">
						<h2>管理</h2>
					</div>
					<!-- End Box Head-->
					
					<div class="box-content">
					<a href="index.php" class="add-button"><span>添加新备忘</span></a>
						<div class="cl">&nbsp;</div>
						
						<p class="select-all"><label></label></p>
						<p><a href="#"></a></p>
						
						<!-- Sort 
						<div class="sort">
							<label>Sort by</label>
							<select class="field">
								<option value="">Title</option>
							</select>
							<select class="field">
								<option value="">Date</option>
							</select>
							<select class="field">
								<option value="">Author</option>
							</select>
						</div>
						-->
						<!-- End Sort -->
						
					</div>
				</div>
				<!-- End Box -->
			</div>
			<!-- End Sidebar -->
			
			<div class="cl">&nbsp;</div>			
		</div>
		<!-- Main -->
	</div>
</div>
<!-- End Container -->

<!-- Footer -->
<div id="footer">
	<div class="shell">
		<span class="left">&copy; 2018 - MagicMirror</span>
		<span class="right">
			Collect form <a href="" target="_blank" title="The Sweetest CSS Templates WorldWide">扩展</a>
		</span>
	</div>
</div>
<!-- End Footer -->

<!-- Latest compiled and minified JavaScript -->
<script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js"></script>
<script src="https://unpkg.com/bootstrap-table@1.14.2/dist/bootstrap-table.min.js"></script>
<script language="javascript" type="text/javascript" src="js/echarts.min.js"></script>
<script>
	$(document).ready(function () {
		$("#result").bootstrapTable({
			columns: [{
					field: 'name',
					title: '用户名'
				}, {
					field: 'type',
					title: '类型'
				}, {
					field: 'title',
					title: '标题'
				}, {
					field: 'content',
					title: '内容'
				},{
					field: 'setTime',
					title: '创建时间'
				},{
					field: 'reTime',
					title: '提醒时间'
				},{
					field: 'delete',
					title: '删除'
				}, ]
		});
	});
</script>

<script language="javascript" type="text/javascript" src="js/weather_line.js"></script>

</body>
</html>