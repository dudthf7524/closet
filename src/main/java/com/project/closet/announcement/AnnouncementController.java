package com.project.closet.announcement;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/announcement")
public class AnnouncementController {

    private final AnnouncementRepository announcementRepository;
    private final AnnouncementService announcementService;

    @GetMapping("/list")
    public String announcementList(
            HttpServletRequest request,
            Model model,
            @RequestParam(name = "page") Integer page,
            @RequestParam(value = "announcementTitle", required = false) String title
    ) {

        System.out.println("page" + page);
        if (page == null || page ==0){
            page = 1;
        }

        Page<AnnouncementEntity> result;

        if (title != null && !title.isEmpty()) {
            result = announcementRepository.findByAnnouncementTitleContaining(title, PageRequest.of(page-1, 10));
        } else {
            result = announcementRepository.findPageBy(PageRequest.of(page-1, 10));
        }
        // 페이지 요청: page-1을 사용하여 0-indexed로 변환


        String option[];
        String URI = request.getRequestURI().substring(1);
        option = URI.split("/");

        System.out.println(URI);

        for (int i = 0 ; i<option.length; i++){
            System.out.println(option[i]);
        }


        // 페이지에 대한 정보 추가
        int totalPages = result.getTotalPages();
        int currentPage = page;

        model.addAttribute("option", option[0]);

        model.addAttribute("announcementEntityList", result.getContent()); // 10개씩 리스트 넘기기
        model.addAttribute("totalPages", totalPages); // 총 페이지 수
        model.addAttribute("currentPage", currentPage); // 현재 페이지
        model.addAttribute("title", title);
        return "announcement/list";
    }

    @GetMapping("/detail/{id}")
    public String announcementDetail(HttpServletRequest request, Model model, AnnouncementEntity announcement){
        String option[];
        String URI = request.getRequestURI().substring(1);
        option = URI.split("/");


        Optional<AnnouncementEntity> optionalAnnouncementEntity = announcementService.findById(announcement.getId());
        AnnouncementEntity announcementEntity = optionalAnnouncementEntity.get();
        model.addAttribute("option", option[0]);
        model.addAttribute("announcement", announcementEntity);

        return "announcement/detail";
    }


}
