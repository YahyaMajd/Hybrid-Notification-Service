package com.example.demo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.cloud.firestore.Firestore;
@Service
public class ReminderScheduler {

    private final FirebaseController firebaseController;

    public ReminderScheduler(FirebaseController firebaseController) {
        this.firebaseController = firebaseController;
    }

    // This cron job will run every minute. Adjust as necessary.
    @Scheduled(cron = "0 * * * * *")  // Run every minute
    public void checkAndSendReminders() {
        try {
            System.out.println("Running Cron");
            firebaseController.checkRemindersAndSendNotifications();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
