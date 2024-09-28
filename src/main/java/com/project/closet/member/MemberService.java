package com.project.closet.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void save(MemberEntity memberEntity) {
        memberRepository.save(memberEntity);
    }



    public String emailCheck(String memberEmail) {
        MemberEntity byMemberEmail = memberRepository.findByMemberid(memberEmail);
        if(byMemberEmail != null){
            //조회결과가 있다 -> 사용할 수 없다
            return null;
        }else{
            //조회결과가 없다 -> 사용할 수 있다.
            return "ok";
        }
    }

    public void updateMemberName(Long id, String membername) {
        memberRepository.updateMemberName(id, membername);
    }

    public Optional<MemberEntity> findbyid(Long memberno) {


        return memberRepository.findById(memberno);
    }

    public void updateMemberPassword(Long id, String membernewpassword) {
        memberRepository.updateMemberPassword(id, membernewpassword);
    }

    public void deletebyId(Long id) {
        memberRepository.deleteById(id);

    }

    public List<MemberEntity> list() {

       return memberRepository.findAll();

    }

    public List<MemberEntity> findByMembernameContains(String memberName) {

        return memberRepository.findByMembernameContains(memberName);

    }
}
