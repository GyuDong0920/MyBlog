<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="isAuthenticated()">
    <sec:authentication property="principal" var="principal"/>
</sec:authorize>
  
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="${_csrf.parameterName}" content="${_csrf.token}">
    <meta name="_csrf_header" content="${_csrf.headerName}">
    <title>Insert title here</title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" />
    <!-- jQuery library -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <!-- Popper JS -->
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <!-- Latest compiled JavaScript -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
    <!-- include summernote css/js -->
	<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.js"></script>
     <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.3/css/all.css" integrity="sha384-UHRtZLI+pbxtHCWp1t77Bi1L4ZtiqrqD80Kn4Z8NTSRyMA2Fd33n5dQ8lWUE00s/" crossorigin="anonymous"></head>
  </head>
  <body>
    <nav class="navbar navbar-expand-md bg-dark navbar-dark">
      <!-- Brand -->
      <a class="navbar-brand" href="/">블로그</a>
      

      <!-- Toggler/collapsibe Button -->
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
        <span class="navbar-toggler-icon"></span>
      </button>

      <!-- Navbar links -->
      <div class="collapse navbar-collapse" id="collapsibleNavbar">
        <ul class="navbar-nav">
          <c:choose>
            <c:when test="${empty principal}">
              <li class="nav-item">
                <a class="nav-link" href="/auth/login_form">로그인</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="/auth/join_form">회원가입</a>
              </li>
            </c:when>
            <c:otherwise>
              <!-- 로그인 성공하면(회원이면) 글 작성 가능 -->
              <li class="nav-item">
                <a class="nav-link" href="/board/save_form">글쓰기</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="/user/update_form">회원정보</a>
              </li>
              <li class="nav-item">
              <!-- 시큐리티를 적용하면 자동 로그아웃 처리 된다. -->
                <a class="nav-link" href="/logout">로그아웃</a>
              </li>
            </c:otherwise>
          </c:choose>
           <li class="nav-item dropdown">
           		<a href="#" class="dropdown-toggle nav-link" data-toggle="dropdown">
           			STORY	
           		</a>
           		<ul class="dropdown-menu">
           			<li class="">
           				<a href="/story/home" class="dropdown-item">
           				  <i class="fas fa-cloud">&nbsp;&nbsp;SHOW STORY</i>
           				</a>
           				
           				<a href="/story/upload" class="dropdown-item">
           				  <i class="fas fa-file">&nbsp;&nbsp;UPLOAD</i>
           				</a>
           			</li>
           		</ul>
           </li>
        </ul>
      </div>
    </nav>
    <!-- 자바 스크립트 추가할 때는 그 페이지의 밑에 작성 -->
