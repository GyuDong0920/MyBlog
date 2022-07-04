<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp" %>

<div class="container">
	<input type="hidden" id="principal--id" value="${principal.user.id}">
	<button class="btn bg-secondary" onclick="history.back();">돌아가기</button>
	<c:if test="${board.userId.id == principal.user.id}">
		<a href="/board/${board.id}/update_form" class="btn btn-warning">수정</a>
		<button class="btn btn-danger" id="btn-delete">삭제</button>	
	</c:if>
	<br/><br/>
	<div>
		글 번호 : <span id="board-id"><i>${board.id}</i></span><br/>
		글 작성자 : <span id=""><i>${board.userId.username}</i></span>
	</div>
	<br/><br/>
	<div class="form-group m-2">
		<h3>${board.title}</h3>
	</div>
	<hr/>
	<div class="form-group m-2">
		<h3>${board.content}</h3>
	</div>	
	<br/><br/>
	<hr/>
	<br/>
	<div class="card">
		<div>
			<div class="card-body"><textarea rows="1" class="form-control" id="reply-content"></textarea></div>
			<div class="card-footer"><button type="button" class="btn btn-primary" id="btn-reply-save">등록</button></div>
		</div>
	</div>
<br/>
	<div class="card">
		<div class="card-header">댓글 목록</div>
	</div>
	<!-- 오류방지를 위해 id에 하이픈(-) 하나 더 붙여준다 -->
	<ul class="list-group" id="reply--box">	
		<c:forEach var="reply" items="${board.replies}">
		<li class="list-group-item d-flex justify-content-between" id="reply--${reply.id}">
			<div>${reply.content}</div>
			<div class="d-flex">
				<!-- 본인 댓글만 삭제할 수 있도록 처리 -->
				<div>작성자 : ${reply.user.username}&nbsp;&nbsp;</div>
				<c:if test="${reply.user.id eq principal.user.id}">
					<button class="badge badge-danger" onclick="index.replyDelete(${board.id}, ${reply.id});">삭제</button>				
				</c:if>
				<!-- ajax 통신할 때는 이벤트 바인딩 onclick으로 처리! (렌더링 순서때문에 이벤트 핸들러가 등록이 안됨) -->
			</div>
		</li>
		</c:forEach>
	</ul>

<br/><br/>
</div>

<script src="/js/board.js"></script>
<%@ include file="../layout/footer.jsp" %>