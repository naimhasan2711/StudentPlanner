package com.binigrmay.studentplanner

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.binigrmay.studentplanner.utils.NotificationHelper
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

/**
 * Application class for StudentPlanner
 */
@HiltAndroidApp
class StudentPlannerApplication : Application(), Configuration.Provider {
    
    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Timber for logging (always enabled for now)
        Timber.plant(Timber.DebugTree())
        
        // Create notification channels
        NotificationHelper.createNotificationChannels(this)
    }
    
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
