package com.example.tencoding.blog.repository;

import java.util.ArrayList;
import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

import com.example.tencoding.blog.dto.ReplyCountOfBoardDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ReplyCountBoardRepository {

	
	private final EntityManager em;
	
	public List<ReplyCountOfBoardDto> getReplyCount() {
		
		List<ReplyCountOfBoardDto> list = new ArrayList<ReplyCountOfBoardDto>();
		
		
		String queryStr = "SELECT A.id, A.content, (SELECT COUNT(boardId) "
                + "            FROM reply AS B "
                + "            WHERE B.boardId = A.id ) AS replyCount "
                + "FROM  board AS A ";

        Query nativeQuery = em.createNativeQuery(queryStr);
		
		// 문자열을 통해 질의어를 생성
		// 1. 직접 문자열을 컨트롤 해서 object 맵핑 방식
		// 2. QLRM 라이브러리를 사용해서 object 맵핑 방식
		
// 		List<Object[]> resultList = nativeQuery.getResultList();	
// 		System.out.println(resultList.toString());
// 		resultList.forEach(t -> {
// 			System.out.println(t.toString());
// 		});
       
        // QLRM 라이브러리 사용
        JpaResultMapper jpaResultMapper = new JpaResultMapper();
        list = jpaResultMapper.list(nativeQuery, ReplyCountOfBoardDto.class);
		
		return list; 
	}
}
