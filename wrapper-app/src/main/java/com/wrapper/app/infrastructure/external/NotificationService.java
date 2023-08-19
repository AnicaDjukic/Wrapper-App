package com.wrapper.app.infrastructure.external;

import com.wrapper.app.application.service.UserService;
import com.wrapper.app.domain.model.User;
import com.wrapper.app.infrastructure.util.EmailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final UserService userService;

    private final EmailSender emailSender;

    public NotificationService(UserService userService, EmailSender emailSender) {
        this.userService = userService;
        this.emailSender = emailSender;
    }

    public void sendNotification(String zipPath, String semestar) {
        emailSender.sendEmail(List.of("wrapper.app@outlook.com"), "Raspored " + semestar, "",  zipPath);
//        List<User> users = userService.getAll();
//        List<String> userEmails = users.stream().map(User::getUsername).toList();
//        emailSender.sendEmail(userEmails, "Raspored", "", zipPath);

    }
}
