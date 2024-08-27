package com.example.demo.Controller;

import com.example.demo.Service.MemberService;
import com.example.demo.entity.Member;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    MemberService memberService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/test/register")
    public Member register(@RequestBody Member member){
        System.out.println(member);
        memberService.register(member);
        return null;
    }

}
