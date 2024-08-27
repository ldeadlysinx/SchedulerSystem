package com.example.demo.Job;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@RequiredArgsConstructor
public class SendMail implements Job {

    private final JavaMailSender mailSender;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //메일 발신자
        String setFrom = "";

        //메일 수신자
        String toMail = "";

        String title = "메일 알림 제목";

        String content
                = //html 형식으로 작성 !
                "<br><br>"
                        + "메일 알림 내용"
                        + "<br>";

        MimeMessage message = mailSender.createMimeMessage();

        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setFrom(setFrom);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content, true);

            mailSender.send(message);


        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
