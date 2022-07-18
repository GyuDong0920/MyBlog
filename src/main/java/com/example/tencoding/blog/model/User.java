package com.example.tencoding.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity // User 클래스가 자동으로 MySQL에 테이블을 생성한다
// @DynamicInsert // Null값이 들어오면 쿼리 제외 -> Defalt값 들어가도록
public class User {

	@Id // Primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에 연결된 DB의 넘버링 전략을 따라가겠다
	private int id;
	@Column(nullable = false, length = 100, unique = true)
	private String username;
	@Column(nullable = false, length = 100)
	private String password;
	@Column(nullable = false, length = 50)
	private String email;
	@CreationTimestamp // 시간이 자동으로 입력된다
	private Timestamp createDate;

	// Domain - 데이터의 범주화 (User, user 등 잘못입력 방지)
	// @ColumnDefault("'user'")
	@Enumerated(EnumType.STRING)
	private RoleType role; // Enum 타입 사용 권장 : admin, user, manager
	
	private String oauth; // kakao, google, naver ...
}
