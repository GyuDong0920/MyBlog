package com.example.tencoding.blog.test;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.tencoding.blog.model.RoleType;
import com.example.tencoding.blog.model.User;
import com.example.tencoding.blog.repository.UserRepository;

@RestController
public class DummyControllerTest {

	// UserRepository 메모리에 올라가있는 상태
	// 그럼 어떻게 가져오나요? --> DI
	@Autowired
	private UserRepository userRepository;
	
	// SelectAll
	@GetMapping("/dummy/users")
	public List<User> 전체사용자검색() {
		
		return userRepository.findAll();
	}
	
	// 페이징 처리
	// http://localhost:8081/blog/dummy/user?page=0
	@GetMapping("/dummy/user")
	public Page<User> pageList(@PageableDefault(size = 1, sort = "id",
			direction = Direction.DESC) Pageable pageable) {
		// 한 페이지당 2개의 값을 들고와라

//		Page page = userRepository.findAll(pageable);
//		List<User> user = page.getContent();
		
		Page<User> pageUser = userRepository.findAll(pageable);				
		return pageUser;
	}
	
	// REST POST
	// Create
	@PostMapping("/dummy/join")
	public String join(@RequestBody User user) {
		
		System.out.println("===============================");
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		System.out.println(user.getEmail());
		System.out.println("===============================");
		
		System.out.println(user.getId());
		System.out.println(user.getCreateDate());
		System.out.println(user.getRole()); // null값 들어감 -> default 값 불가

		user.setRole(RoleType.USER);
		userRepository.save(user);

		return "회원가입 완료 되었습니다.";
	}
	
	// Select
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		
		// Optional --> Optional로 감싸서 User Entity를 가지고 오겠다 (Null safety)
		// User user = userRepository.findById(id).get();
		
//		User user = userRepository.findById(id).orElseGet(() -> {
//			return new User();  // 없으면 새로운 User 객체 던짐
//		});
		
		User user = userRepository.findById(id).orElseThrow(()-> {
			// 없으면 예외 던짐 (예외 처리)
			return new IllegalArgumentException(id + "에 해당하는 유저는 존재하지 않습니다.");
		});	
		
		//User user = userRepository.findById(id);
		
		return user;
	}
	
	@Transactional // 더티체킹 개념때문에 이 어노테이션 걸면 자동으로 update 처리가 된다.
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User reqUser) {
		
		System.out.println("id  : " + id);
		System.out.println("password : " + reqUser.getPassword());
		System.out.println("email : " + reqUser.getEmail());
		// select 해봐서 추가적인 처리
		User userEntity = userRepository.findById(id).orElseThrow(() -> { // 영속성 컨텍스트에 있는 객체 들고오는 것
			// 없는 id값 요청할 때
			return new IllegalArgumentException(id + "에 해당하는 유저는 존재하지 않습니다.");
		});
		// --> DB 저장 // username 없다
		userEntity.setPassword(reqUser.getPassword());
		userEntity.setEmail(reqUser.getEmail());		
		// User user = userRepository.save(userEntity); // 있으면 생성해서 저장 없으면 수정
		return null;
	}
	
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
			
		} catch (Exception e) {
			return "해당 유저는 존재하지 않습니다.";
		}
		
		return id + "는 삭제되었습니다.";
	}
	
}
