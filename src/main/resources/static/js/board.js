let index = {
	init: function() {
		$("#btn-save").bind("click", ()=> {
			this.save(); 
		});
		
		$("#btn-delete").bind("click", ()=> {
			this.deleteById(); 
		});

		$("#btn-update").bind("click", ()=> {
			this.update(); 
		});
		
		$("#btn-reply-save").bind("click", ()=> {
			this.replySave(); 
		});
		

	},
	
	save: function() {
		
		let token = $("meta[name='_csrf']").attr("content");
		let header = $("meta[name='_csrf_header']").attr("content");
		
		// 데이터 가져오기
		let data = {
			title: xSSCheck($("#title").val(), 1),
			content: $("#content").val()
		}
		console.log("데이터 확인");
		console.log(data);
		
		$.ajax({
			beforeSend : function(xhr) {
				console.log("xhr : " + xhr);
				xhr.setRequestHeader(header, token)
			},
			type: "POST",
			url: "/api/board",
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8", // 메타 타입
			dataType: "json" // 응답받는 타입이 json
			
		})
		.done(function(data, textStatus, xhr){
			if(data.status) {
				 alert("글 쓰기가 완료되었습니다.");
				 location.href="/";
			}
		})
		.fail(function(error) {
			alert("글 쓰기에 실패하였습니다.");
		});
	},
	
	deleteById: function() {
		
		let token = $("meta[name='_csrf']").attr("content");
		let header = $("meta[name='_csrf_header']").attr("content");
		
		let id = $("#board-id").text();
		
		$.ajax({
			beforeSend : function(xhr) {
				console.log("xhr : " + xhr);
				xhr.setRequestHeader(header, token)
			},
			type:"DELETE",
			url:"/api/board/" + id
		})
		.done(function(data){
			if(data.status) {
				alert("삭제가 완료되었습니다.");
				location.href = "/";
			}
		})
		.fail(function(){
			alert("삭제에 실패했습니다.");
		});
	},
	
	
	update: function() {
		
		let token = $("meta[name='_csrf']").attr("content");
		let header = $("meta[name='_csrf_header']").attr("content");
		
		let boardId = $("#id").val();
		
		let data = {
			title: $("#title").val(),
			content: $("#content").val()
		}	
		
		$.ajax({
			beforeSend : function(xhr) {
				console.log("xhr : " + xhr);
				xhr.setRequestHeader(header, token)
			},
			type: "PUT",
			url: "/api/board/" + boardId,
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			dataType: "json",
			async: false
		})
		.done(function(data) {
			if(data.status) {
				alert("글 수정이 완료 되었습니다");
				location.href="/";
			}
		})
		.fail(function(error){
			alert("글 쓰기에 실패하였습니다");			
		});
		
	},
	
	// 댓글 등록
	replySave: function() {
		
		// csrf 활성화 후에는 header에 token 값을 넣어야 정상 동작된다.
		let token = $("meta[name='_csrf']").attr("content");
		let header = $("meta[name='_csrf_header']").attr("content");
		
		console.log("token : " + token);
		console.log("header : " + header);
		
		// 데이터 가져오기 (boardId : 해당 게시글의 아이디입니다.)
		let data = {
			boardId: $("#board-id").text(),
			content: $("#reply-content").val()
		}
		console.log("데이터 확인");
		console.log(data);
		
		// ``백틱(자바스크립트 변수를 문자열 안에 넣어서 사용할 수 있다.)
		$.ajax({
			beforeSend : function(xhr) {
				console.log("xhr : " + xhr);
				xhr.setRequestHeader(header, token)
			},
			type: "POST",
			url: `/api/board/${data.boardId}/reply`,
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			dataType: "json"
			
		})
		.done(function(response){ // response dto가 넘어오는데 js에서 1이면 true
			
			if(response.status) {
				console.log(response.data);
				addReplyElement(response.data);
			} else {
				
			}
			
			console.log(response);

			/**
			if(response.status) {
				 alert("댓글 작성이 완료되었습니다.");
				 location.href=`/board/${data.boardId}`;
			}
			 */
			
		})
		.fail(function(error) {
			alert("댓글 작성에 실패하였습니다.");
		});
		
		
	}, // end of replySave
	
	
	
	replyDelete: function(boardId, replyId) {
		
		let token = $("meta[name='_csrf']").attr("content");
		let header = $("meta[name='_csrf_header']").attr("content");
		
		$.ajax({
			beforeSend : function(xhr) {
				console.log("xhr : " + xhr);
				xhr.setRequestHeader(header, token)
			},
			type: "DELETE", 
			url: `/api/board/${boardId}/reply/${replyId}`,
			dataType: "json"
		}).done(function(response) {
			console.log(response);
			alert("댓글 삭제 성공");
			location.href = `/board/${boardId}`;
		}).fail(function(error) {
			console.log(error);
			alert("댓글 삭제 실패");
		});
	}
	
}


function addReplyElement(reply) {
	let principalId = $("#pricipal--id").val();
	let childElement =`<li class="list-group-item d-flex justify-content-between" id="reply--${reply.id}">
	      <div>${reply.content}</div>
	      <div class="d-flex">
	        <div>작성자 : ${reply.user.username}&nbsp;&nbsp;</div>
	        <c:if test="${reply.user.id == principalId }">
				<button class="badge badge-danger" onclick="index.replyDelete(${reply.board.id}, ${reply.id});">삭제</button>
			</c:if>
	      </div>
	    </li>`;
	    
	 $("#reply--box").prepend(childElement);   
	 $("#reply-content").val("");   
}


function xSSCheck(str, level) {
    if (level == undefined || level == 0) {
        str = str.replace(/\<|\>|\"|\'|\%|\;|\(|\)|\&|\+|\-/g,"");
    } else if (level != undefined && level == 1) {
        str = str.replace(/\</g, "&lt;");
        str = str.replace(/\>/g, "&gt;");
    }
    return str;
}


index.init();