<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!-- 页面头部 -->
<header class="main-header">
	<!-- Logo -->
	<a href="${pageContext.request.contextPath}/pages/main.jsp" class="logo"> <!-- mini logo for sidebar mini 50x50 pixels -->
		<span class="logo-mini"><b>数据</b></span> <!-- logo for regular state and mobile devices -->
		<span class="logo-lg"><b>数据</b>后台管理</span>
	</a>
	<!-- Header Navbar: style can be found in header.less -->
	<nav class="navbar navbar-static-top">
		<!-- Sidebar toggle button-->
		<a href="#" class="sidebar-toggle" data-toggle="offcanvas"
			role="button"> <span class="sr-only">Toggle navigation</span>
		</a>

		<div class="navbar-custom-menu">
			<ul class="nav navbar-nav">

				<li class="dropdown user user-menu"><a href="#"
					class="dropdown-toggle" data-toggle="dropdown"> <img
						src="${pageContext.request.contextPath}/img/user2-160x160.jpg"
						class="user-image" alt="User Image">
					<span class="hidden-xs">
							<%--<security:authentication property="principal.username" />--%>
							<security:authentication property="name" />
					</span>
				</a>
					<ul class="dropdown-menu">
						<!-- User image -->
						<li class="user-header"><img
							src="${pageContext.request.contextPath}/img/user2-160x160.jpg"
							class="img-circle" alt="User Image"></li>

						<!-- Menu Footer-->
						<li class="user-footer">
							<div class="pull-left">
								<a href="#" class="btn btn-default btn-flat">修改密码</a>
							</div>
							<div class="pull-right">
								<!--发现网页注销后，竟然是 404
									理由就是  CSRF!!!

									上图中，我们可以发现logout 是个 get 请求
									可是，在 spring security 中 默认的 "/logout" 是 Post 请求 , 因为他觉得 /logout也是需要  CSRF安全保障的
									所以get请求 为404
								<a href="${pageContext.request.contextPath}/logout"
									class="btn btn-default btn-flat">注销</a>
									-->
								<form action="${pageContext.request.contextPath}/logout" method="post">
										<security:csrfInput/>
										<input type="submit" value="注销"/>
								</form>
							</div>
						</li>
					</ul></li>
			</ul>
		</div>
	</nav>
</header>
<!-- 页面头部 /-->