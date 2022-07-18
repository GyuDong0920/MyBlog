package com.example.tencoding.blog.service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.tencoding.blog.dto.RequestFileDto;
import com.example.tencoding.blog.model.Image;
import com.example.tencoding.blog.model.User;
import com.example.tencoding.blog.repository.StoryRepository;


@Service
public class StoryService {

    @Value("${file.path}")
    private String uploadFolder;

    @Autowired
    StoryRepository storyRepository;

    public Page<Image> getImageList(Pageable pageable) {
        return storyRepository.findAll(pageable);
    }

    @Transactional
    public void imageUpload(RequestFileDto fileDto, User user) {

        // 파일 업로드 기능 (해당 서버에 바이너리 파일 생성하고 성공하면 DB 저장)
        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + "story";
        
        String newFileName = (imageFileName.trim()).replaceAll("\\s", "");
        
        System.out.println("파일명 : " + imageFileName.trim());

        // 서버 컴퓨터의 path를 가지고 와야 한다.(경로)
        Path imageFilePath = Paths.get(uploadFolder + newFileName);
        System.out.println("전체 파일 경로 + 파일명 : " + imageFilePath);

        try {
            Files.write(imageFilePath, fileDto.getFile().getBytes());

            Image imageEntity = fileDto.toEntity(newFileName, user);
            storyRepository.save(imageEntity);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }






}