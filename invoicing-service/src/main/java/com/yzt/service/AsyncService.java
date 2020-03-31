package com.yzt.service;

import javax.mail.MessagingException;
import java.io.IOException;

public interface AsyncService {

    void sendMail() throws MessagingException;
}
