<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/common/global.jsp"%>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/include-base-styles.jsp" %>
	<title>已部署流程定义列表--process</title>

	<script src="${ctx }/js/common/jquery.js" type="text/javascript"></script>
</head>
<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success">${message}</div>
		<!-- 自动隐藏提示信息 -->
		<script type="text/javascript">
		setTimeout(function() {
			$('#message').hide('slow');
		}, 5000);
		</script>
	</c:if>
	<fieldset id="deployFieldset">
		<legend>部署流程资源</legend>
		<form action="${ctx }/process/deploy" method="post" enctype="multipart/form-data" style="margin-top:1em;">
			<input type="file" name="file" />
			<input type="submit" value="Submit" class="btn" />
		</form>
		<hr class="soften" />
	</fieldset>
	<form action="${ctx }/process/process-list" method="post" class="form-inline">
		<label class="control-label">名称:</label>
		<input type="text" id="name" name="name" value="">
		<label class="control-label">Key:</label>
		<input type="text" id="key" name="key" value="">
		<input type="submit" value="查询">
	</form>
	<table width="100%" class="table table-bordered table-hover table-condensed">
		<thead>
			<tr>
				<th>流程定义ID</th>
				<th>部署ID</th>
				<th>流程定义名称</th>
				<th>流程定义KEY</th>
				<th>版本号</th>
				<th style="width:300px;">XML资源名称</th>
				<th style="width:250px;">图片资源名称</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.result}" var="pd">
				<tr>
					<td>${pd.id }</td>
					<td>${pd.deploymentId }</td>
					<td>${pd.name }</td>
					<td>${pd.key }</td>
					<td>${pd.version }</td>
					<td style="width:300px;"><a target="_blank" href='${ctx }/process/read-resource?pdid=${pd.id }&resourceName=${pd.resourceName }'>${pd.resourceName }</a></td>
					<td style="width:250px;"><a target="_blank" href='${ctx }/process/read-resource?pdid=${pd.id }&resourceName=${pd.diagramResourceName }'>${pd.diagramResourceName }</a></td>
					<td>
						<a class="btn" href='${ctx }/process/delete-deployment?deploymentId=${pd.deploymentId }'><i class="icon-trash"></i>删除</a>
						<a class="btn" href='${ctx }/process/getform/start/${pd.id }'><i class="icon-play"></i>启动</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<tags:pagination page="${page}" paginationSize="${page.pageSize}"/>
</body>
</html>