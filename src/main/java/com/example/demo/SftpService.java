package com.example.demo;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpSession;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;

@Service
@Slf4j
public class SftpService {
    public void uploadFile(String filename, InputStream inputStream){
        SftpSession session = gimmeFactory().getSession();

        try {
//            FileInputStream fileInputStream = new FileInputStream(new File(path));
            session.write(inputStream, "upload/" + filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        session.close();
    }

    public String download(String filename){
        String absolutefilename = "upload/" + filename;
        String tmpFile =  "tmp/" + filename;
        JSch jsch = new JSch();
        try {
            Session session = jsch.getSession("mike", "0.0.0.0", 22);
            session.setPassword("password123");
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            ChannelSftp sftp = (ChannelSftp)session.openChannel("sftp");
            sftp.connect();
            sftp.lcd("tmp");
            sftp.get(absolutefilename, filename);
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }

        return tmpFile;
    }

    private DefaultSftpSessionFactory gimmeFactory(){
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory();
        factory.setHost("0.0.0.0");
        factory.setPort(22);
        factory.setAllowUnknownKeys(true);
        factory.setUser("mike");
        factory.setPassword("password123");
        return factory;
    }





}
