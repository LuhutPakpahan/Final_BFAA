package robert.pakpahan.final_bfaa.respond.notification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import robert.pakpahan.final_bfaa.MainActivity

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra("title")
        val message = intent?.getStringExtra("message")

        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)

    context?.let {
            AlarmUtil.showNotification(
                context = context,
                title = title ?: "Alarm Title",
                message = message ?: "Alarm Message",
                notificationId = 0,
                pendingIntent = pendingIntent
            )
        }
    }

    companion object {
        var TAG = AlarmReceiver::class.java.simpleName
    }

}