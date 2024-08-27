package com.example.demo.Service;

import com.example.demo.Repository.MemberRepository;
import com.example.demo.entity.Member;
import org.springframework.stereotype.Service;

@Service

public class MemberService {

    MemberRepository memberRepository;

    public void register(Member member){
       memberRepository.save(member);
    }

}
