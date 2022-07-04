package com.example.tencoding.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tencoding.blog.model.Board;

public interface BoardRepository extends JpaRepository<Board, Integer> {
	
	// 명명 규칙
	// SELECT * FROM board WHERE title LIKE '%title%';
	Page<Board> findByTitleContaining(String title, Pageable pageable);
}
