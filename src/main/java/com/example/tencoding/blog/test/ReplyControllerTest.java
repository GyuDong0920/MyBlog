package com.example.tencoding.blog.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.tencoding.blog.dto.ReplyCountOfBoardDto;
import com.example.tencoding.blog.model.Board;
import com.example.tencoding.blog.model.Reply;
import com.example.tencoding.blog.repository.BoardRepository;
import com.example.tencoding.blog.repository.ReplyCountBoardRepository;
import com.example.tencoding.blog.repository.ReplyRepository;

@RestController
public class ReplyControllerTest {

	@Autowired
	ReplyCountBoardRepository replyCountBoardRepository;
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private ReplyRepository replyRepository;
	
	@GetMapping("/test/board/{boardId}")
	public Board getBoard(@PathVariable int boardId) {
		// Jackson 라이브러리 실행될 때 오브젝트로 파싱 (json을 파싱할 때 getter를 호출)
		// 무한 참조 발생 (Board 안에 Reply, Reply 안에 Board ... )
		return boardRepository.findById(boardId).get();
	}
	
	/**
	 * 
	 * board 호출했을 때 reply에 포함된 board를 무시하고
	 * Reply에서 호출했을 때는 무시하지 않는다.
	 * 
	 * detail.jsp에서 reply.baord 호출하는 순간 무한 참조가 일어난다. (stack overflow 발생)
	 * 하지만 호출하지 않았기 때문에 발생하지 않았다.
	 * 
	 * 해결 방법은 JsonIgnoreProperties 사용
	 */
	@GetMapping("/test/reply")
	public List<Reply> getReply() {
		return replyRepository.findAll();
	}
	
	// .../test/group-by-count
	@GetMapping("/test/group-by-count3")
	public String test4() {
		
		List<ReplyCountOfBoardDto> result = replyCountBoardRepository.getReplyCount();
		
		return "" + result.toString();
	}
	
}
