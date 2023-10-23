package fr.eseo.e5e.ag.whatcolour

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class NotificationService : FirebaseMessagingService() {
  // Method to get user token on fist install

  override fun onNewToken(token: String) {
    super.onNewToken(token)
    Log.d("FCM", "Refreshed token: $token")
  }
}
