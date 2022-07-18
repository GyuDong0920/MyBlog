package com.example.tencoding.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.tencoding.blog.auth.PrincipalDetail;
import com.example.tencoding.blog.dto.RequestFileDto;
import com.example.tencoding.blog.model.Image;
import com.example.tencoding.blog.service.StoryService;



@Controller
@RequestMapping("/story")
public class StoryController {

    @Autowired
    StoryService storyService;

    @GetMapping("/home")
    public String storyHome(Model model, @PageableDefault(size = 10, sort = "id", direction = Direction.DESC) Pageable pageable) {
        Page<Image> imagePage = storyService.getImageList(pageable);
        model.addAttribute("imagePage", imagePage);
        return "/story/home";
    }

    @GetMapping("/upload")
    public String storyUpload() {
        return "/story/upload";
    }

    @PostMapping("/image/upload")
    public String storyImageUpload(RequestFileDto fileDto, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        storyService.imageUpload(fileDto, principalDetail.getUser());
        return "redirect:/story/home";
    }

}