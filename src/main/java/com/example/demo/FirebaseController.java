package com.example.demo;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirebaseController {

    @Autowired
    private PushNotificationService pushNotificationService;

    @GetMapping("/test-push")
    public String testPushNotification() {
        try {
            // Replace this with a real Firebase device token for testing
            String token = "fHUCEWDvTBGEoFyuI1Oe4Y:APA91bEQ_m95eBpNLZ8CpakQ8Go8wsKqRbu9J_3J89iL6S2c9ftL_TDxMqTfIlHmogV4SGMZwkKP0opAm_jaTTKzEK3PWowfjDaQ75ZVKcQnfIcBdfxe1naz_A7_rUwblQf89J2It1gD";
            pushNotificationService.sendPushNotification(token, "Test Title", "Test Body");
            return "Push notification sent!";
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return "Failed to send push notification.";
        }
    }
}
