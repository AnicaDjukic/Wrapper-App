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

    public void sendNotificationToAllUsers(String zipPath, String semestar) {
        List<User> users = userService.getAll();
        List<String> userEmails = users.stream().map(User::getUsername).toList();
        emailSender.sendEmail(userEmails, "Raspored za " + semestar, "",  zipPath);

    }

    public void sendNotification(String email, String zipPath, String semestar) {
        emailSender.sendEmail(List.of(email), "Raspored za " + semestar, "", zipPath);
    }
}
