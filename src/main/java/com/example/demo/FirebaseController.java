// package com.example.demo;

// import com.google.firebase.messaging.FirebaseMessagingException;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RestController;

// @RestController
// public class FirebaseController {

//     @Autowired
//     private PushNotificationService pushNotificationService;

//     @GetMapping("/test-push")
//     public String testPushNotification() {
//         try {
//             // Replace this with a real Firebase device token for testing
//             String token = "fHUCEWDvTBGEoFyuI1Oe4Y:APA91bEQ_m95eBpNLZ8CpakQ8Go8wsKqRbu9J_3J89iL6S2c9ftL_TDxMqTfIlHmogV4SGMZwkKP0opAm_jaTTKzEK3PWowfjDaQ75ZVKcQnfIcBdfxe1naz_A7_rUwblQf89J2It1gD";
//             pushNotificationService.sendPushNotification(token, "Test Title", "Test Body");
//             return "Push notification sent!";
//         } catch (FirebaseMessagingException e) {
//             e.printStackTrace();
//             return "Failed to send push notification.";
//         }
//     }

// }
package com.example.demo;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class FirebaseController {

    @Autowired
    private PushNotificationService pushNotificationService;

    

    @Autowired
    private Firestore db;

    @GetMapping("/test-push")
    @ResponseBody
    public String testPushNotification() {
        try {
            //User token
            String tokenTest = "fHUCEWDvTBGEoFyuI1Oe4Y:APA91bEQ_m95eBpNLZ8CpakQ8Go8wsKqRbu9J_3J89iL6S2c9ftL_TDxMqTfIlHmogV4SGMZwkKP0opAm_jaTTKzEK3PWowfjDaQ75ZVKcQnfIcBdfxe1naz_A7_rUwblQf89J2It1gD";
            pushNotificationService.sendPushNotification(tokenTest, "Test Title", "Test Body");
            return "Push notification sent!";
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return "Failed to send push notification.";
        }
    }

    // called every minute by cron in reminderscheduler
    public void checkRemindersAndSendNotifications() {
        long currentTime = System.currentTimeMillis();

        try {
            // Query users' reminders
            CollectionReference usersCollection = db.collection("users");
            ApiFuture<QuerySnapshot> usersQuery = usersCollection.get();
            List<QueryDocumentSnapshot> users = usersQuery.get().getDocuments();

            for (QueryDocumentSnapshot userDoc : users) {
                CollectionReference reminders = userDoc.getReference().collection("reminders");

                // Query reminders where the dateTime is less than or equal to the current time
                ApiFuture<QuerySnapshot> remindersQuery = reminders.whereLessThanOrEqualTo("dateTime", currentTime).get();
                List<QueryDocumentSnapshot> reminderDocs = remindersQuery.get().getDocuments();

                for (QueryDocumentSnapshot reminderDoc : reminderDocs) {
                    String title = reminderDoc.getString("title");
                    String message = reminderDoc.getString("message");
                    System.out.println("Reminder title :" +  title);
                    // Retrieve the user's FCM token and send the notification
                    String token = getUserFirebaseToken(userDoc.getId());
                    pushNotificationService.sendPushNotification(token, title, message);

                    // Optionally delete the reminder after sending the notification
                    reminderDoc.getReference().delete();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to get user's Firebase token
    private String getUserFirebaseToken(String userId) {
        // Logic to fetch user's FCM token from Firestore or any DB
        // Implement actual logic to retrieve the token from Firestore
        try {
            DocumentReference userDocRef = db.collection("users").document(userId);
            ApiFuture<DocumentSnapshot> future = userDocRef.get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.getString("fcmToken");
            } else {
                System.out.println("No such user!");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
