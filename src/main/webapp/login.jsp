<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>Activiti Explorer</title>
	<%@ include file="/common/global.jsp"%>
	<%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/include-base-styles.jsp" %>
</head>

<body style="margin-top: 3em;">
	<center>
	<c:if test="${not empty param.error}">
		<h2 id="error" class="alert alert-error">用户名或密码错误！！！</h2>
	</c:if>
	<c:if test="${not empty param.timeout}">
		<h2 id="error" class="alert alert-error">未登录或超时！！！</h2>
	</c:if>
	<div style="width: 500px">
		<h2>Login</h2>
		<form action="${ctx}/user/logon" method="get" class="form-horizontal">
			<div class="control-group">
				<label class="control-label" for="username">用户名：</label>
				<div class="controls">
					<input id="username" name="username" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="password">密码：</label>
				<div class="controls">
					<input id="password" name="password" type="password" />
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<button type="submit" class="btn btn-primary">登录系统</button>
				</div>
			</div>
		</form>
	</div>
	</center>
</body>
</html>