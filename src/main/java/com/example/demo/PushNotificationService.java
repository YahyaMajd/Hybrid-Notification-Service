package com.example.demo;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationService {

    // Method to send push notification to a specific device using a device token
    public void sendPushNotification(String token, String title, String body) throws FirebaseMessagingException {
        // Create a notification payload
        Notification notification = Notification.builder()
            .setTitle(title)
            .setBody(body)
            .build();

        // Create a message with the specified token and notification payload
        Message message = Message.builder()
            .setToken(token)
            .setNotification(notification)
            .build();

        // Send the message via Firebase Messaging
        FirebaseMessaging.getInstance().send(message);
    }
}
