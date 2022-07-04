<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>

<div class="container">
	<form action="#" method="post">
	<input type="hidden" id="id" value="${principal.user.id}">
		<div class="form-group">
			<label for="username">username: </label>
			<input type="text" value="${principal.user.username}" id="username" name="username" class="form-control" readonly="readonly">
		</div>
			<c:if test="${empty principal.user.oauth}">
		<div class="form-group">
			<label for="password">password: </label>
			<input type="password" value="" id="password" name="password" class="form-control">
		</div>
		
		<div class="form-group">
			<label for="email">email: </label>
			<input type="email" value="${principal.user.email}" id="email" name="email" class="form-control">
		</div>
		</c:if>
		<button type="button" class="btn btn-primary" id="btn-update">회원정보 수정</button>
	</form>
</div>
<br/><br/>
<script src="/js/user.js"></script>

<%@ include file="../layout/footer.jsp" %>