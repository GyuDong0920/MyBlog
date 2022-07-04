package com.example.tencoding.blog.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.tencoding.blog.model.Board;
import com.example.tencoding.blog.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	// @RequestParam 안 써도 스프링에서 기본으로 key=value로 매핑됨
	@GetMapping({"", "/", "/board/search"})
	public String index(String q, Model model,
			@PageableDefault(size=3, sort="id", direction = Direction.DESC) Pageable pageable) {
		
		String searchTitle = q == null ? "" : q;
		
		Page<Board> pageBoards = boardService.searchBoardByTitle(searchTitle, pageable);
		
		/**
		 * 
		 * 1. 현재 페이지의 앞 뒤로 2 블록(칸)씩 보이기
		 * 2. 페이지 버튼(블록)을 누르면 해당 페이지로 화면 이동하기 
		 * 3. 현재 페이지 'active' 하기 (UX)
		 * 
		 */
		
		int nowPage = pageBoards.getPageable().getPageNumber() + 1; // 현재 페이지
		int startPage = Math.max(nowPage - 2, 1); // 두 int 값 중에 큰 값 반환
		int endPage = Math.min(nowPage + 2, pageBoards.getTotalPages());
		
		/*
		log.info("현재 화면의 블록 숫자(현재 페이지) : {}", nowPage);
		log.info("현재 화면의 보여질 블록의 시작 번호 : {}", startPage);
		log.info("현재 화면에 보여질 마지막 블록의 번호 : {}", endPage);
		log.info("한 화면에 보여질 게시글 수 / 총 게시글 수 : {}", pageBoards.getTotalPages());
		*/
		
		// 시작 페이지를 설정해야 한다

		// 페이지 번호를 배열로 만들어서 던져주기
		ArrayList<Integer> pageNumbers = new ArrayList<>();
		// 주의! 마지막 번호까지 저장하기
		for (int i = startPage; i <= endPage; i++) {
			pageNumbers.add(i);
		}
		
		model.addAttribute("pageable", pageBoards);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("pageNumbers", pageNumbers);
		model.addAttribute("searchTitle", searchTitle);
		
		return "index";
	}
	
	@GetMapping("/board/save_form")
	public String saveForm() {
		log.info("saveForm 메서드 호출");
		return "/board/save_form";
	}
	
	@GetMapping("/board/{id}")
	public String findById(@PathVariable int id, Model model) {
		model.addAttribute("board", boardService.boardDetail(id));
		return "/board/detail";
	}
	
	// 수정 화면 가져오기
	@GetMapping("/board/{id}/update_form")
	public String updateForm(@PathVariable int id, Model model) {
		model.addAttribute("board", boardService.boardDetail(id));
		return "/board/update_form";
	}
	
	
	
}
