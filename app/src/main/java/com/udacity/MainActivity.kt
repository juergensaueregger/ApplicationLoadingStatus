package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private lateinit var notificationManager: NotificationManager
    private lateinit var binding: ActivityMainBinding
    private var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(toolbar)
        createChannel(getString(R.string.channel_name), getString(R.string.channel_name))
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        binding.contentMain.customButton.setOnClickListener {
            val buttonId = binding.contentMain.radioGroup.checkedRadioButtonId
            if (buttonId != -1) {
                url = getUrl(buttonId) ?: ""
                binding.contentMain.radioGroup.clearCheck()
                binding.contentMain.customButton.buttonState = ButtonState.Clicked
                download()
            } else {
                Toast.makeText(this, getString(R.string.toast_text), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.getUriForDownloadedFile(id)
            binding.contentMain.customButton.buttonState = ButtonState.Completed
            notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager

            notificationManager.sendNotification(
                "something great happend",
                context,
                "SUCCESSFUL",
                url
            )
            Toast.makeText(context, getString(R.string.download_ready), Toast.LENGTH_SHORT).show()
        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request)
    }

    private fun getUrl(ButtonId: Int): String? = when (ButtonId) {
        R.id.glide_rb -> URL["glide"]
        R.id.retrofit_rb -> URL["retro"]
        R.id.loadapp_rb -> URL["app"]
        else -> ""
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH

            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "your download notification"
            notificationChannel.apply {
                setShowBadge(false)
            }


            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


    companion object {
        private val URL = mapOf<String, String>(
            "glide" to "https://github.com/bumptech/glide/archive/refs/heads/master.zip",
            "retro" to "https://github.com/square/retrofit/archive/refs/heads/master.zip",
            "app" to "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        )
    }

}
