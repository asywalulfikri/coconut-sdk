package app.beelabs.com.codebase.callback

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.datatheorem.android.trustkit.reporting.BackgroundReporter
import com.datatheorem.android.trustkit.reporting.PinningFailureReport


internal class PinningFailureReportBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        val report = intent.getSerializableExtra(BackgroundReporter.EXTRA_REPORT) as PinningFailureReport
    }
}
