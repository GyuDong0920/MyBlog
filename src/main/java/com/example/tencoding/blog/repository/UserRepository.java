package com.example.tencoding.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tencoding.blog.model.User;

// DAO
// Bean으로 등록될 수 있나요? --> 스프링에서 Ioc에서 객체를 가지고 있나요?
public interface UserRepository extends JpaRepository<User, Integer>{
	
	// spring JPA 네이밍 전략
	// SELECT *  FROM user  WHERE username = ?1 AND password = ?2;
	// 네이밍 전략 따라가면 쿼리가 만들어진다
	// User findByUsernameAndPassword(String username, String password);
	
	// 두번째 방법
	// @Query(value = "SELECT * FROM user  WHERE username = ?1 AND password = ?2")
	
	// SELECT * FROM user WHERE username = 1?;
	Optional<User> findByUsername(String username);

}
