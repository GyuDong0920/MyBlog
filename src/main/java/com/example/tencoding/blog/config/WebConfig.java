package com.example.tencoding.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;

@Configuration
public class WebConfig implements WebMvcConfigurer{

	@Value("${file.path}")
	private String upLoadFolder;
	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		WebMvcConfigurer.super.addResourceHandlers(registry);
		// file:///C:/springimageDir/tencoblog/upload/3fac9da8-2705-4178-9d0e-e9947f9d65f0_asd.jpg
		System.out.println("\"file:///\" + upLoadFolder" + "file:///" + upLoadFolder);
		registry.addResourceHandler("/upload/**") // URL 패턴 /upload/ -> 낚아챔
		.addResourceLocations("file:///"+ upLoadFolder) // 실제 물리적 경로
		.setCachePeriod(60 * 10 * 6) // 캐시의 지속시간 설정(초)
		.resourceChain(true)
		.addResolver(new PathResourceResolver());// 리소스 찾는것을 최적화 하기 위해서 addResolver(new PathResourceResolver());
	// 가상 경로를 설정해서 http url로 접근하면 실제 컴퓨터에 있는 경로로
	}
	
	
	@Bean // IoC 등록
	public FilterRegistrationBean<XssEscapeServletFilter> filterRegistrationBean() {
		
		FilterRegistrationBean<XssEscapeServletFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(new XssEscapeServletFilter());
		filterRegistrationBean.setOrder(1);
		filterRegistrationBean.addUrlPatterns("/*"); // 먹히는 범위 설정
		
		return filterRegistrationBean;
	}

}
