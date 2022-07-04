package com.example.tencoding.blog.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false, length = 100)
	private String title;
	@Lob // 대용량 데이터
	private String content;
	@ColumnDefault("0") // int // String " '안녕' "
	private int count; // 조회수

	// 여러 개의 게시글은 하나의 유저를 가진다
	// Many == Board, One == User
	@ManyToOne(fetch = FetchType.EAGER) // Board select 한번에 데이터를 가져와
	@JoinColumn(name = "userId")
	private User userId;

	@CreationTimestamp
	private Timestamp createDate;
	
	// 댓글 정보
	// 하나의 게시글에 여러 개의 댓글이 있을 수 있다.
	// one = board, many = reply
	// mappedBy = "board" board는 reply 테이블의 필드 이름이다.
	// mappedBy - 나는 연관관계의 주인이 아니다 (FK)
	// DB에 컬럼을 만들지 마시오
	@OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE) // LAZY - 필요할 때 요청해서 들고올 수 있다
	@JsonIgnoreProperties({"board", "content"}) // reply 안에 있는 board getter를 무시해라 (getter 호출 안됨)
	@OrderBy("id DESC")
	private List<Reply> replies;

}
