package com.example.tencoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tencoding.blog.model.RoleType;
import com.example.tencoding.blog.model.User;
import com.example.tencoding.blog.repository.UserRepository;


@Service // IoC 등록
public class UserService {
	
	/**
	 * 
	 * 서비스가 필요한 이유
	 * 
	 * 현실 모델링 : 레스토랑 주문 -> 서버(추가적인 요청) -> 주방장 음식 조리
	 * 
	 * 트랜잭션 관리
	 * 송금 : 홍길동, 이순신
	 * 홍길동(10), 이순신(0) --> 홍길동 (select) --> 이순신(5) (insert)
	 * 여러 가지 트랜잭션들이 모여 하나의 서비스를 만든다.
	 * 
	 * 하나의 기능 + 하나의 기능을 묶어서 단위의 기능을 만들어내기 위해 필요하다
	 * 하나의 기능 (서비스가 될 수 있다.)
	 * 
	 */

	// DI 의존 주입
	@Autowired // 초기화 처리까지 되는 것
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Transactional
	public int saveUser(User user) {
		// 이런 것들을 묶는 하나의 기능을 만들 때
		try {
			String rawPassword = user.getPassword();
			String encPassword = encoder.encode(rawPassword);		
			user.setPassword(encPassword);
			user.setRole(RoleType.USER);
			userRepository.save(user); // Service -> Repository
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 1;		
	}

	@Transactional
	public void updateUser(User user) {

		// 카카오가 수정이 들어 오면 무시
		// 기존 수정이 들어오면 처리
		
			User userEntity = userRepository.findById(user.getId()).orElseThrow(() -> {
				return new IllegalArgumentException("회원정보가 없습니다.");
			});
			
			if (userEntity.getOauth() == "" || userEntity.getOauth() == null) {
			// 해시 암호화 처리
			String rawPassword = user.getPassword();
			String hashPassword = encoder.encode(rawPassword);
			
			userEntity.setPassword(hashPassword);
			userEntity.setEmail(user.getEmail());
			// 더티체킹 (@Transactional)
		} else {
			
		}
	}
	
	// 가입된 사용자인지 확인
	// readOnly = true : select일 경우
	@Transactional(readOnly = true)
	public User searchUser(String username) {
		User userEntity = userRepository.findByUsername(username).orElseGet(() -> {
			return new User();
		});
		return userEntity;
	}
	
	/*
	@Transactional(readOnly = true)
	public User login(User user) {
		// repository select 요청
		return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
	}
	*/
}
