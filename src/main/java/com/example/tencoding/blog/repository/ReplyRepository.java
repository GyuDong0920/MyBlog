package com.example.tencoding.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tencoding.blog.model.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Integer>{
	

}
