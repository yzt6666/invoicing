package com.yzt.util;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class DataBaseUtil {

    private static final String root = "root";
    private static final String pwd = "123456";
    private static final String dbName = "invoicing";
    private static final String backPath = "F:/";

    /**
     * 数据备份
     * @param backName
     * @return
     * @throws IOException
     */
    public static File backup(String backName) throws IOException {
        String pathSql = backPath+backName;
        File fileSql = new File(pathSql);
        //创建备份sql文件
        if (!fileSql.exists()){
            fileSql.createNewFile();
        }
        //mysqldump -hlocalhost -uroot -p123456 db > /home/back.sql
        StringBuffer sb = new StringBuffer();
        sb.append("mysqldump");
        sb.append(" -h127.0.0.1");
        sb.append(" -u"+root);
        sb.append(" -p"+pwd);
        sb.append(" "+dbName+" >");
        sb.append(pathSql);
        Runtime runtime = Runtime.getRuntime();
        runtime.exec("cmd /c"+sb.toString());
        return fileSql;
    }

    /**
     * 数据恢复
     * @param file
     * @throws IOException
     * @throws InterruptedException
     */
    public static void dbRestore(MultipartFile file) throws IOException, InterruptedException {
        String filePath = "F://" + file.getOriginalFilename();
        FileUtils.copyInputStreamToFile(file.getInputStream(), new File(filePath));
        Thread.sleep(3000);
        StringBuilder sb = new StringBuilder();
        sb.append("mysql");
        sb.append(" -h127.0.0.1");
        sb.append(" -u"+root);
        sb.append(" -p"+pwd);
        sb.append(" "+dbName+" <");
        sb.append(filePath);
        Runtime runtime = Runtime.getRuntime();
        runtime.exec("cmd /c"+sb.toString());
    }

}
