package com.project.closet.manager;

import com.project.closet.announcement.AnnouncementEntity;
import com.project.closet.announcement.AnnouncementRepository;
import com.project.closet.announcement.AnnouncementService;
import com.project.closet.member.MemberEntity;
import com.project.closet.member.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
public class ManagerController {
    private final MemberService memberService;

    private final AnnouncementService announcementService;

    private final AnnouncementRepository announcementRepository;


        @GetMapping()
    public String list(HttpServletRequest request, Model model){

        String option[];
        String URI = request.getRequestURI().substring(1);
        option = URI.split("/");
        model.addAttribute("option", option[0]);

        return "/manager/list";
    }

    @GetMapping("/member/list")
    public String memberList(HttpServletRequest request,
                             Model model,
                             @RequestParam(value = "membername", required = false) String memberName){
        System.out.println("회원이름" + memberName);
        String option[];
        String URI = request.getRequestURI().substring(1);
        option = URI.split("/");

        List<MemberEntity> memberEntityList = memberService.list();

        if(memberName == null || memberName.equals("")){
            memberEntityList = memberService.list();
        }else{
            memberEntityList = memberService.findByMembernameContains(memberName);
        }




        for (int i = 0; i<memberEntityList.size();i++){
            memberEntityList.get(i).setNo(i+1);
            String oauthMember[] = memberEntityList.get(i).getMemberid().split("_");
            memberEntityList.get(i).setMemberid(oauthMember[0]);
        }

        model.addAttribute("option", option[0]);
        model.addAttribute("memberEntityList", memberEntityList);
        model.addAttribute("membername", memberName);
        return "manager/member/list";

    }

    @GetMapping("/announcement/write")
    public String announcementWrite(HttpServletRequest request, Model model){

        String option[];
        String URI = request.getRequestURI().substring(1);
        option = URI.split("/");
        model.addAttribute("option", option[0]);


        return "/manager/announcement/write";
    }

    @PostMapping("/announcement/write")
    public String announcementWriteForm(HttpServletRequest request, Model model, AnnouncementEntity announcement){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);

        for (int i =1; i<101; i++){
            AnnouncementEntity newAnnouncement = new AnnouncementEntity();

            newAnnouncement.setAnnouncementTitle(announcement.getAnnouncementTitle()+i);
            newAnnouncement.setAnnouncementContent(announcement.getAnnouncementContent()+i);
            newAnnouncement.setWriteDate(formattedDate);
            announcementService.announcementSave(newAnnouncement);
        }

        announcement.setWriteDate(formattedDate);



        return "redirect:/manager/announcement/list";
    }



    @GetMapping("/announcement/list")
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

        // 페이지에 대한 정보 추가
        int totalPages = result.getTotalPages();
        int currentPage = page;

        model.addAttribute("option", option[0]);

        model.addAttribute("announcementEntityList", result.getContent()); // 10개씩 리스트 넘기기
        model.addAttribute("totalPages", totalPages); // 총 페이지 수
        model.addAttribute("currentPage", currentPage); // 현재 페이지
        model.addAttribute("title", title);
        return "/manager/announcement/list";
    }

    @GetMapping("/announcement/detail/{id}")
    public String announcementDetail(HttpServletRequest request, Model model, AnnouncementEntity announcement){
        String option[];
        String URI = request.getRequestURI().substring(1);
        option = URI.split("/");


        Optional<AnnouncementEntity> optionalAnnouncementEntity = announcementService.findById(announcement.getId());
        AnnouncementEntity announcementEntity = optionalAnnouncementEntity.get();
        model.addAttribute("option", option[0]);
        model.addAttribute("announcement", announcementEntity);

        return "manager/announcement/detail";
    }

    @GetMapping("/announcement/edit/{id}")
    public String announcementEdit(HttpServletRequest request, Model model, AnnouncementEntity announcement){
        String option[];
        String URI = request.getRequestURI().substring(1);
        option = URI.split("/");


        Optional<AnnouncementEntity> optionalAnnouncementEntity = announcementService.findById(announcement.getId());
        AnnouncementEntity announcementEntity = optionalAnnouncementEntity.get();
        model.addAttribute("option", option[0]);
        model.addAttribute("announcement", announcementEntity);

        return "/manager/announcement/edit";
    }

    @PostMapping("/announcement/edit")
    public String announcementEditForm(HttpServletRequest request, Model model, AnnouncementEntity announcement){


        announcementService.announcementSave(announcement);




        return "redirect:/manager/announcement/list";
    }
    @GetMapping("/announcement/delete/{id}")
    public String announcementDelete(HttpServletRequest request, Model model, AnnouncementEntity announcement){


        announcementService.deleteById(announcement.getId());




        return "redirect:/manager/announcement/list";
    }


}
