package com.ruike.itf.infra.util;

import com.jcraft.jsch.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

/**
 * <p>
 * Sftp工具类
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/6 10:37
 */
public class SftpUtils {

    private static final Logger logger = LoggerFactory.getLogger(SftpUtils.class);

    private static Session getSession(String host, String user, Integer port, String password) {
        JSch jsch = new JSch();
        Session session;
        try {
            session = jsch.getSession(user, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect(30000);
        } catch (JSchException e) {
            logger.error(Arrays.toString(e.getStackTrace()));
            return null;
        }
        return session;
    }

    private static ChannelSftp getSftpChannel(String host, String user, Integer port, String password) {
        Session session = getSession(host, user, port, password);
        if (Objects.nonNull(session)) {
            ChannelSftp sftp;
            try {
                Channel channel = session.openChannel("sftp");
                channel.connect();
                sftp = (ChannelSftp) channel;
            } catch (JSchException e) {
                logger.error(Arrays.toString(e.getStackTrace()));
                return null;
            }
            return sftp;
        } else {
            return null;
        }
    }

    public static XSSFWorkbook downloadExcel(String host, String user, Integer port, String password, String path, String file) throws Exception {
        ChannelSftp sftp = getSftpChannel(host, user, port, password);
        if (Objects.isNull(sftp)) {
            return null;
        }

        // 切换到文件所在目录
        sftp.cd(path);
        //获取文件并返回给输入流,若文件不存在该方法会抛出常
        InputStream inputStream = sftp.get(file);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        // 关闭流
        inputStream.close();

        // 关闭sftp连接
        Session session = sftp.getSession();
        sftp.disconnect();
        session.disconnect();
        return workbook;
    }
}
