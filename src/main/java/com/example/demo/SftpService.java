package com.example.demo;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class SftpService {
    public void uploadFile(String filename, InputStream inputStream){
        val session = gimmeFactory().getSession();

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
        val absolutefilename = "upload/" + filename;
        val tmpFile =  "tmp/" + filename;
        val jsch = new JSch();
        try {
            val session = jsch.getSession("mike", "0.0.0.0", 22);
            session.setPassword("password123");
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            val sftp = (ChannelSftp)session.openChannel("sftp");
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
        val factory = new DefaultSftpSessionFactory();
        factory.setHost("0.0.0.0");
        factory.setPort(22);
        factory.setAllowUnknownKeys(true);
        factory.setUser("mike");
        factory.setPassword("password123");
        return factory;
    }





}
