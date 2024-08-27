package com.example.demo.Job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class BackupDatabase implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String backupDir = "F:\\mysqlbackup\\";
        String dbName = "demo";
        String dbUser = "root";
        String dbPass = "1234";

        String backupFileName = backupDir + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_backup.sql";

        String executeCmd = String.format("C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump -u%s -p%s %s -r %s", dbUser, dbPass, dbName, backupFileName);

        // 명령어를 실행하기 위해 Runtime 객체를 사용하여 새로운 프로세스를 생성합니다.
        Process runtimeProcess;
        try {
            // executeCmd는 mysqldump 명령어를 포함하고 있으며, MySQL 데이터베이스의 백업을 수행합니다.
            runtimeProcess = Runtime.getRuntime().exec(executeCmd);

            // 명령어가 실행된 후 프로세스가 완료될 때까지 대기합니다.
            // waitFor() 메서드는 프로세스가 종료될 때까지 현재 스레드를 대기 상태로 만듭니다.
            // 프로세스가 종료되면 종료 상태 코드를 반환합니다.
            int processComplete = runtimeProcess.waitFor();

            // 프로세스가 성공적으로 완료되었는지 확인합니다.
            // 프로세스가 정상적으로 완료되면 waitFor()은 0을 반환합니다.
            // 이 경우 백업이 성공적으로 생성되었다는 메시지를 출력합니다.
            if (processComplete == 0) {
                System.out.println("성공적으로 백업 하였습니다."+LocalDateTime.now());
            } else {
                System.out.println("백업을 실패 하였습니다"+LocalDateTime.now());
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
