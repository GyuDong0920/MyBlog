package com.example.tencoding.blog.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.tencoding.blog.auth.PrincipalDetail;
import com.example.tencoding.blog.dto.ResponseDto;
import com.example.tencoding.blog.model.Board;
import com.example.tencoding.blog.model.Reply;
import com.example.tencoding.blog.service.BoardService;

@RestController
public class BoardApiController {
	
	@Autowired
	BoardService boardService;
	// 문제인식
	// 세션을 어떻게 가져와야 하는가?
	
	// 1. 주소 매핑, 데이터 받기
	@PostMapping("/api/board")
	public ResponseDto<Integer> save(@RequestBody Board board, @AuthenticationPrincipal PrincipalDetail detail) {
		boardService.write(board, detail.getUser());
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@DeleteMapping("/api/board/{id}")
	public ResponseDto<Integer> deleteById(@PathVariable int id) {
		boardService.deleteById(id);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("/api/board/{id}")
	public ResponseDto<Integer> update(@PathVariable int id, @RequestBody Board board) {
		boardService.modifyBoard(id, board);
		return new ResponseDto<>(HttpStatus.OK.value(), 1);
	}	
	
	@PostMapping("/api/board/{boardId}/reply")
	public ResponseDto<Reply> replySave(@PathVariable int boardId,
			@RequestBody Reply reply, @AuthenticationPrincipal PrincipalDetail principalDetail) {
		
		// 서비스에 넘겨서 데이터 처리
		Reply replyEntity = boardService.writeReply(principalDetail.getUser(), boardId, reply);
		return new ResponseDto<Reply>(HttpStatus.OK.value(), replyEntity); // 200일 때 replyEntity 리턴한다.
	}
	
	@DeleteMapping("/api/board/{boardId}/reply/{replyId}")
	public ResponseDto<Integer> deleteReply(@PathVariable int boardId, @PathVariable int replyId) {
		boardService.deleteReplyById(replyId);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
}
