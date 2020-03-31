package com.yzt.service.com.yzt.service.impl;

import com.yzt.mapper.AsyncMapper;
import com.yzt.service.AsyncService;
import com.yzt.util.ExcelUtil;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AsyncServiceImpl implements AsyncService {
    @Resource
    private JavaMailSenderImpl mailSender;
    @Resource
    private AsyncMapper asyncMapper;
    /*
        定时发送邮件
        cron: 秒 分 时 日 月 周几
     */
    @Scheduled(cron = "0 10 0 * * 0-7")
    @Override
    public void sendMail() throws MessagingException{
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String subject = sdf.format(date) + "销售情况";
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
        mimeMessageHelper.setFrom("565872132@qq.com");
        mimeMessageHelper.setTo("565872132@qq.com");
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(subject, true);
        List<Map> list = asyncMapper.selTodaySale();
        String[] title = {"产品编号", "产品名称", "销售量", "销售额"};
        String sheetName = "销售情况表";
        System.out.println(list);
        String[][] content = new String[list.size()][title.length];
        for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);
            content[i][0] = String.valueOf(map.get("productID"));
            content[i][1] = String.valueOf(map.get("productName"));
            content[i][2] = String.valueOf(map.get("quantity"));
            content[i][3] = String.valueOf(map.get("totalPrice"));
        }
        ExcelUtil excelUtil = new ExcelUtil();
        XSSFWorkbook workbook = excelUtil.getHSSFWorkbook(sheetName, title, content, null, null, null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            workbook.write(out);
            byte[] bytes = out.toByteArray();
            is = new ByteArrayInputStream(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mimeMessageHelper.addAttachment("销售情况表.xlsx", new ByteArrayResource(IOUtils.toByteArray(is)), "application/vnd.ms-excel;charset=UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mailSender.send(mimeMessage);
    }
}
