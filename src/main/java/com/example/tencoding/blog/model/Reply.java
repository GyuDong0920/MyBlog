package com.example.tencoding.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reply {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false, length = 200)
	private String content;

	@ManyToOne // 여러 개의 댓글은 하나의 게시글에 존재할 수 있다
	@JoinColumn(name = "boardId")
	@JsonIgnoreProperties({"replies", "userId"})
	private Board board;

	// 하나의 댓글에 여러 유저가 작성을 할 수 있는가?
	@ManyToOne
	@JoinColumn(name = "userId") // <---- Reply 테이블의 컬럼명이 된다
	@JsonIgnoreProperties({"password", "email", "oauth", "role"})
	private User user;

	@CreationTimestamp
	private Timestamp createDate;

}
