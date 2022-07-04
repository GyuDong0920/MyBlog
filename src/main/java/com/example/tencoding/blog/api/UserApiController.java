package com.example.tencoding.blog.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.tencoding.blog.dto.ResponseDto;
import com.example.tencoding.blog.model.User;
import com.example.tencoding.blog.service.UserService;

// JSON 리턴
@RestController
public class UserApiController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	// @ResponseBody JSON 방식으로 데이터를 받겠다

	@PutMapping("/user")
	public ResponseDto<Integer> update(@RequestBody User user) {
		userService.updateUser(user);
		System.out.println(user);

		// 강제로 Authentication 객체를 만들고 SecurityContext 안에 집어넣으면 된다.
		// 1. Authentication 객체를 생성
		// 2. AuthenticationManager 메모리에 올려서 authenticate 메서드를 사용해서 Authentication 객체를 저장한다.
		// 3. 세션 - SecurityContextHolder.getContext().setAuthentication()에
		// 만들어놓은 Authentication 객체를 넣어주면 된다.

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

}
