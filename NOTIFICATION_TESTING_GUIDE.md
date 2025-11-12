# Notification Testing Guide

## ✅ What Was Fixed

### Issues Found:
1. **ViewModels weren't calling ReminderScheduler** - Tasks and lectures were being saved to the database but reminders were never scheduled
2. **No logging** - Made it difficult to debug notification issues
3. **Notification channels not created on app startup** - Required for Android 8.0+

### Changes Made:

#### 1. **TaskViewModel.kt** - Now schedules/cancels reminders
- ✅ Calls `ReminderScheduler.scheduleTaskReminder()` when inserting tasks
- ✅ Reschedules reminders when updating tasks
- ✅ Cancels reminders when deleting tasks
- ✅ Added comprehensive logging

#### 2. **LectureViewModel.kt** - Now schedules/cancels reminders
- ✅ Calls `ReminderScheduler.scheduleLectureReminder()` when inserting lectures
- ✅ Reschedules reminders when updating lectures
- ✅ Cancels reminders when deleting lectures
- ✅ Added comprehensive logging

#### 3. **ReminderScheduler.kt** - Added detailed logging
- ✅ Logs when reminders are scheduled
- ✅ Shows exact notification times
- ✅ Warns if reminder time has passed
- ✅ Shows delay calculations

#### 4. **TaskReminderWorker.kt** - Added logging
- ✅ Logs when notification is triggered
- ✅ Shows task details
- ✅ Logs success/failure

#### 5. **NotificationHelper.kt** - Enhanced logging & error handling
- ✅ Logs notification creation
- ✅ Checks permissions before posting
- ✅ Catches SecurityExceptions
- ✅ Shows notification details

#### 6. **StudentPlannerApplication.kt** - Creates notification channels on startup
- ✅ Ensures notification channels exist before scheduling

---

## 🧪 How to Test Notifications

### Test 1: Task Reminder (Short delay for testing)

1. **Open the app and go to Tasks tab**

2. **Create a test task:**
   - Title: "Test Task Notification"
   - Due Date: Set to **2 minutes from now**
   - Enable Reminder: **ON**
   - Remind me: **1 minute before** (so notification in 1 minute)

3. **Check Logcat for these logs:**
   ```
   TaskViewModel: Inserting task: Test Task Notification
   TaskViewModel: Task inserted with ID: 1
   TaskViewModel: Scheduling reminder for task: Test Task Notification
   ReminderScheduler: ========== SCHEDULING TASK REMINDER ==========
   ReminderScheduler: Task ID: 1
   ReminderScheduler: Reminder Enabled: true
   ReminderScheduler: Delay (ms): 60000 (1 minutes)
   ReminderScheduler: ✅ Task reminder scheduled successfully!
   ```

4. **Wait 1 minute**

5. **You should see:**
   - Notification appears: "Task Reminder: Test Task Notification"
   - Logcat shows:
     ```
     TaskReminderWorker: ========== TASK REMINDER TRIGGERED ==========
     TaskReminderWorker: Task ID: 1
     TaskReminderWorker: ✅ Notification shown successfully!
     NotificationHelper: ========== SHOWING TASK NOTIFICATION ==========
     NotificationHelper: ✅ Notification posted successfully!
     ```

---

### Test 2: Longer Task Reminder

1. **Create another task:**
   - Title: "Important Assignment"
   - Due Date: **Tomorrow at 2:00 PM**
   - Enable Reminder: **ON**
   - Remind me: **1 hour before**

2. **Check Logcat:**
   ```
   ReminderScheduler: Due Date: Nov 13, 2025 14:00:00
   ReminderScheduler: Reminder Time: Nov 13, 2025 13:00:00
   ReminderScheduler: Will trigger at: Nov 13, 2025 13:00:00
   ```

3. **Verify:** The reminder is scheduled for 1:00 PM tomorrow

---

### Test 3: Edit Task & Update Reminder

1. **Edit an existing task**
2. **Change reminder time** from "1 hour before" to "2 hours before"
3. **Save**

4. **Check Logcat:**
   ```
   TaskViewModel: Updating task: Important Assignment
   TaskViewModel: Rescheduling reminder for task: Important Assignment
   ReminderScheduler: ========== SCHEDULING TASK REMINDER ==========
   ReminderScheduler: ✅ Task reminder scheduled successfully!
   ```

---

### Test 4: Delete Task & Cancel Reminder

1. **Delete a task with reminder enabled**

2. **Check Logcat:**
   ```
   TaskViewModel: Deleting task: Important Assignment
   TaskViewModel: Cancelling reminder for deleted task: Important Assignment
   ```

---

### Test 5: Lecture Reminder

**Note:** Lectures currently only support reminders for specific dates (not recurring)

1. **Create a lecture with specific date:**
   - Title: "Math Lecture"
   - Set a specific date/time
   - Enable Reminder: **ON**
   - Remind me: **15 minutes before**

2. **Check Logcat for scheduling confirmation**

---

## 📱 Checking Notification Permissions

### Android 13+ (API 33+):
The app will automatically request notification permission on first launch.

### Manual Check:
1. Go to **Settings → Apps → StudentPlanner**
2. Tap **Notifications**
3. Ensure **All StudentPlanner notifications** is enabled
4. Check both channels are enabled:
   - ✅ Task Reminders
   - ✅ Lecture Reminders

---

## 🐛 Troubleshooting

### Issue: "No notification appears"

**Check Logcat for:**

1. **Permission denied:**
   ```
   NotificationHelper: ❌ No notification permission!
   ```
   **Solution:** Enable notifications in Settings

2. **Reminder time has passed:**
   ```
   ReminderScheduler: Reminder time has already passed. Not scheduling.
   ```
   **Solution:** Set due date in the future

3. **Reminder not enabled:**
   ```
   ReminderScheduler: Reminder not enabled or reminder time is null. Skipping.
   ```
   **Solution:** Toggle "Enable Reminder" switch

### Issue: "Notification appears but with no sound/vibration"

**Check:**
1. Go to **Settings → Apps → StudentPlanner → Notifications**
2. Tap **Task Reminders** or **Lecture Reminders**
3. Ensure:
   - Alert style: **Make sound and pop on screen**
   - Sound: Enabled
   - Vibration: Enabled
   - Do Not Disturb: Check if it's overriding

---

## 📊 Understanding the Logs

### When Creating a Task:
```
TaskViewModel: Inserting task: Test Task
TaskViewModel: Task inserted with ID: 5
TaskViewModel: Scheduling reminder for task: Test Task
ReminderScheduler: ========== SCHEDULING TASK REMINDER ==========
ReminderScheduler: Task ID: 5
ReminderScheduler: Current Time: Nov 12, 2025 15:30:00
ReminderScheduler: Due Date: Nov 13, 2025 14:00:00
ReminderScheduler: Reminder Time: Nov 13, 2025 13:00:00
ReminderScheduler: Delay (ms): 77400000 (1290 minutes)
ReminderScheduler: ✅ Task reminder scheduled successfully!
```

**What this means:**
- Current time: Nov 12, 3:30 PM
- Task due: Nov 13, 2:00 PM
- Reminder: 1 hour before = Nov 13, 1:00 PM
- Delay: 1290 minutes (21.5 hours from now)

### When Notification Triggers:
```
TaskReminderWorker: ========== TASK REMINDER TRIGGERED ==========
TaskReminderWorker: Task ID: 5
TaskReminderWorker: Title: Test Task
TaskReminderWorker: Showing notification...
TaskReminderWorker: ✅ Notification shown successfully!
NotificationHelper: ========== SHOWING TASK NOTIFICATION ==========
NotificationHelper: ✅ Notification posted successfully!
```

---

## ✨ Summary

### What Works Now:
✅ Task reminders are scheduled when tasks are created
✅ Task reminders are rescheduled when tasks are updated
✅ Task reminders are cancelled when tasks are deleted
✅ Lecture reminders work (for specific dates only)
✅ Comprehensive logging for debugging
✅ Notification channels created on app startup
✅ Proper permission handling

### Notification Timing:
- **Tasks:** Notification triggers at: `Due Date - Reminder Time`
- **Example:** Due tomorrow at 3 PM, remind 1 hour before = notification at 2 PM tomorrow

### Testing Recommendations:
1. Always check Logcat to verify reminders are being scheduled
2. Start with short delays (1-2 minutes) to test quickly
3. Verify notification permission is granted
4. Check notification channel settings in system settings
5. Test editing and deleting to ensure reminders are updated/cancelled

---

## 🔍 Logcat Filter Commands

To view only notification-related logs in Android Studio:

**Filter by Tag:**
```
tag:ReminderScheduler|TaskReminderWorker|LectureReminderWorker|NotificationHelper|TaskViewModel|LectureViewModel
```

**Or use package filter:**
```
package:com.binigrmay.studentplanner
```

**Quick test command (ADB):**
```bash
adb logcat | grep -E "ReminderScheduler|TaskReminderWorker|NotificationHelper"
```
