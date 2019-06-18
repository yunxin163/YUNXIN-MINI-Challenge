<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />
	<title>Free CSS template Collect from moobnn.com</title>
	<link rel="stylesheet" href="css/style.css" type="text/css" media="all" />
</head>
<body>
<!-- Header -->
<div id="header">
	<div class="shell">
		<!-- Logo + Top Nav -->
		<div id="top">
			<h1><a href="#">管理员表</a></h1>
			<div id="top-navigation">
			    <?php
				session_start();
				
				echo "欢迎来到智能镜 ".$_SESSION['views'];

				?>
				
				<a href="dongtailiebiao.php">管理员系统</a>
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
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<th><input type='checkbox' class='checkbox' />全选</th>
								<th>用户名</th>
								<th>用户密码</th>
								<th>登录状态</th>
								<th>禁止登录</th>
							</tr>
						    <?php
								$con=mysqli_connect("localhost","root","crb","myDB");
								if(!$con){
								die("连接失败！");
								}
							
								//$total;
								//$result=mysqli_query($con,"select count(*) as userid from user");
								$uname = $_SESSION['views'];
								$result=mysqli_query($con,"select * from user");
								while($row=mysqli_fetch_array($result)){
									echo "<tr class='odd';>";
									echo "<td><input type='checkbox' class='checkbox' /></td>";
									echo "<td><h3><a href=''>${row["userid"]}</a></h3></td>";
									echo "<td>${row["password"]}</td>";
									echo "<td>${row["logstatus"]}</td>";
									$userid=$row['userid'];
									echo "<td><a href='delog.php?userid=$userid&amp;status=0'>禁止登录</a></td>";
									/*echo "<td><a href='' class='ico del'>Delete</a><a href='' class='ico edit'>Edit</a></td>";
									echo "</tr>";
									*/
								}
		                        //查询完毕，记得人走带门
								mysqli_close($con);
                           ?>
						</table>
						
						<!-- Pagging -->
						<div class="pagging">
							<div class="left">
								<a href='delog.php?userid=row["userid"]&amp;status=1'>恢复登录</a>
								
						    </div>
							<div class="right">
								<a href='delog.php?userid=row["userid"]&amp;status=0'>禁止登录</a>
							</div>
						</div>
						<!-- End Pagging -->
						
					</div>
					<!-- Table -->
					
				</div>
				<!-- End Box -->
				
				<!-- Box -->
				<div class="box">
					<!-- Box Head -->
					<div class="box-head">
						<h2>添加新记录</h2>
					</div>
					<!-- End Box Head -->
					
					<form action="" method="post">
						
						<!-- Form -->
						<div class="form">
								<p>
									<span class="req">max 100 symbols</span>
									<label>Article Title <span>(Required Field)</span></label>
									<input type="text" class="field size1" />
								</p>	
								<p class="inline-field">
									<label>Date</label>
									<select class="field size2">
										<option value="">23</option>
									</select>
									<select class="field size3">
										<option value="">July</option>
									</select>
									<select class="field size3">
										<option value="">2009</option>
									</select>
								</p>
								
								<p>
									<span class="req">max 100 symbols</span>
									<label>Content <span>(Required Field)</span></label>
									<textarea class="field size1" rows="10" cols="30"></textarea>
								</p>	
							
						</div>
						<!-- End Form -->
						
						<!-- Form Buttons -->
						<div class="buttons">
							<input type="button" class="button" value="preview" />
							<input type="submit" class="button" value="submit" />
						</div>
						<!-- End Form Buttons -->
					</form>
				</div>
				<!-- End Box -->

			</div>
			<!-- End Content -->
			
			<!-- Sidebar -->
			<div id="sidebar">
				
				<!-- Box -->
				<div class="box">
					
					<!-- Box Head -->
					<div class="box-head">
						<h2>Management</h2>
					</div>
					<!-- End Box Head-->
					
					<div class="box-content">
						<a href="index.php" class="add-button"><span>Add new Article</span></a>
						<div class="cl">&nbsp;</div>
						
						<p class="select-all"><input type="checkbox" class="checkbox" /><label>查找全部</label></p>
						<p><a href="#">删除所选</a></p>
						
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
		<span class="left">&copy; 2010 - CompanyName</span>
		<span class="right">
			Collect form <a href="" target="_blank" title="The Sweetest CSS Templates WorldWide">扩展</a>
		</span>
	</div>
</div>
<!-- End Footer -->
	
</body>
</html>