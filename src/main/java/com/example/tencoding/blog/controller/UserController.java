package com.example.tencoding.blog.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.example.tencoding.blog.dto.KakaoProfile;
import com.example.tencoding.blog.dto.KakaoProfile.KakaoAccount;
import com.example.tencoding.blog.dto.OAuthToken;
import com.example.tencoding.blog.model.User;
import com.example.tencoding.blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// 페이지 리턴
@Controller
public class UserController {
	
	@Value("${tenco.key}")
	private String tencoKey;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HttpSession httpSession;

	@GetMapping("/auth/login_form")
	public String loginForm() {		
		return "user/login_form";
	}
	
	@GetMapping("/auth/join_form")
	public String joinForm() {
		
		return "user/join_form";
	}	
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if(authentication != null) {
				new SecurityContextLogoutHandler().logout(request, response, authentication);
			}
			return "redirect:/";
		}
	
	@GetMapping("/user/update_form")
	public String updateForm() {
		return "user/update_form";
	}
	
	// 스프링 부트 기본 데이터 파싱 전략 key=value
	@PostMapping("/auth/joinProc")
	public String save(User user) {
		userService.saveUser(user);
		return "redirect:/";
	}
	
	// @ResponseBody 데이터 리턴, 페이지 리턴x
	@GetMapping("/auth/kakao/callback")
	public String kakaoCallback(@RequestParam String code) {
		// POST
		// 통신 -- 인증서버
		// Retrofit2
		// OkHttp
		// HttpsURLConnect
		// RestTemplete
		RestTemplate rt = new RestTemplate();
		
		// http 메시지 -> POST
		// 시작줄
		// http header
		// http body

		// 헤더 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// 바디 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		// 값 넣기
		params.add("grant_type", "authorization_code");
		params.add("client_id", "a70d73b27fdb21a75ea8ae3078059058");
		params.add("redirect_uri", "http://localhost:9090/auth/kakao/callback");
		params.add("code", code);
		
		// 헤더와 바디를 하나의 오브젝트로 담아야 한다.
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers); // body, 헤더
		
		// Http 요청 - Post 방식 - 응답
		ResponseEntity<String> response =
				rt.exchange("https://kauth.kakao.com/oauth/token",
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class);
		
		// response -> object 타입으로 변환 (Gson, Json Simple ..)
		// 파싱처리
		OAuthToken authToken = null;
		ObjectMapper objectMapper = new ObjectMapper();
		// String --> Object (클래스 생성)
		try {
			authToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		System.out.println(authToken);
		
		// 엑세스 토큰 사용
		
		RestTemplate rt2 = new RestTemplate();
		
		HttpHeaders headers2 = new HttpHeaders();
		// 주의 Bearer 다음에 무조건 한 칸 띄우기
		headers2.add("Authorization", "Bearer " + authToken.getAccessToken());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// 바디 없이 헤더만 넣기
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);
				
		// JSON --> Object 파싱하기 (KakaoProfile.class)
		ResponseEntity<KakaoProfile> kakaoProfileResponse = rt2.exchange("https://kapi.kakao.com/v2/user/me",
						HttpMethod.POST,
						kakaoProfileRequest,
						KakaoProfile.class);
		
		// 소셜 로그인 처리
		// 사용자가 로그인 했을 경우 최초 사용자라면 -> 회원가입 처리
		// 한번이라도 가입 진행이 된 사용자라면 -> 로그인 처리
		// 만약 회원가입시 필요한 정보가 더 있어야 된다면 추가로 사용자한테 정보를 받아서 가입 진행 해야한다

		KakaoAccount account = kakaoProfileResponse.getBody().getKakaoAccount();
		System.out.println("카카오 아이디 : " + kakaoProfileResponse.getBody().getId());
		System.out.println("카카오 이메일 : " + account.getEmail());
		
		// 블로그 아이디 - 카카오로 로그인시 중복 방지
		System.out.println("블로그에서 사용될 username : " + account.getEmail() + "_" + kakaoProfileResponse.getBody().getId());
		System.out.println("블로그에서 사용될 email : " + account.getEmail());
		
		User kakaoUser = User.builder()
				.username(account.getEmail() + "_" + kakaoProfileResponse.getBody().getId())
				.password(tencoKey) // 절대 노출되면 안됨, 소셜 가입자들은 패스워드가 모두 같다
				.email(account.getEmail())
				.oauth("kakao")
				.build();
		
		System.out.println(kakaoUser);
		
		// UserService 호출해서 저장 진행
		// 단, 소셜 로그인 요청자가 이미 가입된 유저라면 저장x
		
		User originUser = userService.searchUser(kakaoUser.getUsername());
		
		if(originUser.getUsername() == null) {
			System.out.println("신규 회원이므로 회원가입을 진행");
			userService.saveUser(kakaoUser);
		}
		
		// 자동 로그인 처리 -> 시큐리티 세션에다가 강제 저장
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), tencoKey));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return "redirect:/";
	}
}
