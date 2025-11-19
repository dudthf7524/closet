package com.project.closet.member;

import com.project.closet.alert.MessageDTO;
import com.project.closet.oauth.CustomOauth2UserDetails;
import com.project.closet.oauth.MemberRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final PasswordEncoder passwordEncoder;



    @GetMapping("/member/join")
    public String join() {

        LocalDateTime.now();
        System.out.println(LocalDateTime.now());
        return "member/join";

    }

    @PostMapping("member/join")
        public String joinForm(@ModelAttribute MemberEntity memberEntity) {


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = LocalDateTime.now().format(formatter);


            memberEntity.setJoinDate(formattedDate);
            var hash = passwordEncoder.encode(memberEntity.getMemberpassword());
            memberEntity.setMemberpassword(hash);
            memberEntity.setRole(MemberRole.USER);
            memberService.save(memberEntity);

            return "member/login";
        }

    @GetMapping("/member/login")
    public String login() {
        return "member/login";
    }

    @PostMapping("/member/login")
    public String loginForm(@ModelAttribute MemberEntity memberEntity) {



        return "member/login";
    }


    @PostMapping("/member/email-check")
    public @ResponseBody String emailCheck(@RequestParam("memberid") String memberid){
        System.out.println("memberEmail = " + memberid);

        String result = "이메일 주소를 정확히 입력해주세요.";

        if(memberid.equals("")|| memberid==null){
           result =  "이메일 주소를 정확히 입력해주세요.";
           return result;
        }else if(!memberid.matches("^(?=.*[a-z])(?=.*\\d)[a-z0-9]{8,20}$")) {
            result = "8~20자의 영문 소문자, 숫자만 가능합니다.";
            return result;
        }

        String checkResult = memberService.emailCheck(memberid);

        if(checkResult == null){
            result = "중복 이메일 입니다.";
            return result;
        }else if(checkResult.equals("ok")){
            result = "ok";
            return result;
        }else{
            return result;
        }

    }

    @PostMapping("/member/password-check")
    public @ResponseBody String passwordCheck(@RequestParam("memberpassword") String memberpassword){
        System.out.println("memberpassword = " + memberpassword);

        String result = "";

        if(memberpassword.equals("")|| memberpassword==null){
            result =  "비밀번호를 입력해주세요";
            return result;
        }else if(!memberpassword.matches("^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*])[a-z\\d!@#$%^&*]{8,20}$")) {
            result = "8~20자의 영문, 숫자, 특수문자를 조합하여 입력해주세요.";
            return result;
        }
        return "ok";
    }

    @PostMapping("/member/name-check")
    public @ResponseBody String nameCheck(@RequestParam("membername") String membername){
        System.out.println("membername = " + membername);

        String result = "";

        if(membername.equals("")|| membername==null){
            result =  "이름을 입력해주세요";
            return result;
        }else if(!membername.matches("^[가-힣]{1,10}$")) {
            result = "이름은 공백없이 10자리 이하로 입력해주세요";
            return result;
        }
        return "ok";
    }

    @PostMapping("/loginError")
    public String loginError(Model model) {
        String fail = "아이디 또는 비밀번호를 확인해주세요";
        model.addAttribute("fail", fail);
        return "member/login";
    }

    @GetMapping("/member/mypage")
    public String mypage(Authentication auth, Model model,HttpServletRequest request) {
        Long memberno = 0L;
        var result1 =  auth.getAuthorities();
        System.out.println(result1);
        String login = result1.toString();

        if (login.equals("[일반유저]")){
            CustomUser result = (CustomUser)auth.getPrincipal();
            memberno = result.memberno;
        }else{
            CustomOauth2UserDetails customOauth2UserDetails = (CustomOauth2UserDetails)auth.getPrincipal();
            memberno = customOauth2UserDetails.getId();
        }

        Optional<MemberEntity> optionalMemberEntity = memberService.findbyid(memberno);
        MemberEntity member =  optionalMemberEntity.get();

        String option[];
        String URI = request.getRequestURI().substring(1);
        option = URI.split("/");

        model.addAttribute("option", option[1]);
        model.addAttribute("member", member);
        return "member/mypage";
    }


    @GetMapping("/alert")
    public String alert(@ModelAttribute MessageDTO messageDTO, Model model){

        messageDTO.setMessage("아이디와 비밀번호를 다시 입력해주세요");
        messageDTO.setAlert("로그인 실패");
        messageDTO.setRedirectUri("/member/mypage");

        model.addAttribute("messageDTO", messageDTO);

        return ".common/alert";
    }

    @PostMapping("/member/saveChangePassword")
    public String saveChangePassword(@RequestParam(name = "id") Long id,
                                     @RequestParam(name = "memberpassword") String memberpassword,
                                     @RequestParam(name = "membernewpassword") String membernewpassword,
                                     @ModelAttribute MessageDTO messageDTO,
                                     Model model,
                                     HttpServletRequest request,
                                     HttpServletResponse response
    ){
        System.out.println(memberpassword);
        System.out.println(membernewpassword);

        Optional<MemberEntity> optionalMemberEntity = memberService.findbyid(id);
        MemberEntity member =  optionalMemberEntity.get();

        if(passwordEncoder.matches(memberpassword,member.getMemberpassword())){
            if(passwordEncoder.matches(membernewpassword,member.getMemberpassword())){
                messageDTO.setMessage("이전 비밀번호와 새 비밀번호가 같습니다.");
                messageDTO.setAlert("비밀번호 변경 실패");
                messageDTO.setRedirectUri("/member/mypage");

                model.addAttribute("messageDTO", messageDTO);

                return ".common/alert";
            }else{
                var hash = passwordEncoder.encode(membernewpassword);
                memberService.updateMemberPassword(id, hash);

                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null) {
                    new SecurityContextLogoutHandler().logout(request, response, auth);
                }

                return "member/login";
            }
        }else{
            messageDTO.setMessage("이전 비밀번호가 다릅니다.");
            messageDTO.setAlert("비밀번호 변경 실패");
            messageDTO.setRedirectUri("/member/mypage");

            model.addAttribute("messageDTO", messageDTO);

            return ".common/alert";
        }

    }

    @PostMapping("/member/saveChangeName")
    public String saveChangeName(@RequestParam(name = "id") Long id, @RequestParam(name = "membername") String membername){
        memberService.updateMemberName(id, membername);
        return "redirect:/member/mypage";
    }

    @GetMapping("/member/delete")
    public String delete(Authentication auth, Model model) {

        var result1 =  auth.getAuthorities();
        String login = result1.toString();
        String option = "";
        Long id = 0L;

        if(login.equals("[일반유저]")){
            option = "session";
        }else{
            option ="oauth";
        }
        System.out.println(option);
        model.addAttribute("option", option);

        return "member/delete";
    }


    @PostMapping("/member/delete")
    public String memberDelete(Authentication auth,
                               @RequestParam(value = "memberpassword", required = false) String memberpassword,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               @ModelAttribute MessageDTO messageDTO,
                               Model model

    ){
        var result1 =  auth.getAuthorities();
        String login = result1.toString();
        String option = "";
        Long id = 0L;

        // 일반 유저일 경우
        if (login.contains("일반유저")) {
            if (auth.getPrincipal() instanceof CustomUser) { // 객체 타입 확인
                CustomUser result = (CustomUser) auth.getPrincipal();
                Optional<MemberEntity> optionalMemberEntity = memberService.findbyid(result.memberno);

                if (optionalMemberEntity.isPresent()) {
                    MemberEntity member = optionalMemberEntity.get();

                    // 비밀번호 일치 여부 확인
                    if (passwordEncoder.matches(memberpassword, member.getMemberpassword())) {
                        memberService.deletebyId(member.getId());

                        // 로그아웃 처리
                        new SecurityContextLogoutHandler().logout(request, response, auth);

                        // 메시지 설정
                        messageDTO.setMessage("회원탈퇴가 완료되었습니다.");
                        messageDTO.setAlert("회원탈퇴");
                        messageDTO.setRedirectUri("/member/login");

                        model.addAttribute("messageDTO", messageDTO);
                        return ".common/alert";
                    } else {
                        return "member/delete"; // 비밀번호 불일치 시 다시 탈퇴 페이지로
                    }
                } else {
                    // 회원 정보가 없을 경우 예외 처리
                    return "redirect:/error";
                }
            } else {
                return "redirect:/error"; // 예상치 못한 타입의 principal이 반환될 경우
            }
        }
        // OAuth2 유저일 경우
        else {
            if (auth.getPrincipal() instanceof CustomOauth2UserDetails) { // 객체 타입 확인
                CustomOauth2UserDetails customOauth2UserDetails = (CustomOauth2UserDetails) auth.getPrincipal();
                memberService.deletebyId(customOauth2UserDetails.getId());

                // 로그아웃 처리
                new SecurityContextLogoutHandler().logout(request, response, auth);

                // 메시지 설정
                messageDTO.setMessage("회원탈퇴가 완료되었습니다.");
                messageDTO.setAlert("회원탈퇴");
                messageDTO.setRedirectUri("/member/login");

                model.addAttribute("messageDTO", messageDTO);
                return ".common/alert";
            } else {
                return "redirect:/error"; // 예상치 못한 타입의 principal이 반환될 경우
            }
        }

    }
    @PostMapping("/member/email-check-login")
    public @ResponseBody String emailCheckLogin(@RequestParam("memberid") String memberid){
        System.out.println("memberEmail = " + memberid);

        String result = "이메일 주소를 정확히 입력해주세요.";

        if(memberid.equals("")|| memberid==null){
            result =  "이메일 주소를 정확히 입력해주세요.";
            return result;
        }else if(!memberid.matches("^(?=.*[a-z])(?=.*\\d)[a-z0-9]{8,20}$")) {
            result = "8~20자의 영문 소문자, 숫자만 가능합니다.";
            return result;
        }else{
            result = "ok";
            return result;
        }

    }

}
