
package org.brainote.async;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import org.brainote.BaseActivity;
import org.brainote.OmniNotes;
import org.brainote.db.DbHelper;
import org.brainote.models.Note;
import org.brainote.utils.ReminderHelper;

import roboguice.util.Ln;

import java.util.List;


public class AlarmRestoreOnRebootService extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Ln.i("System rebooted: service refreshing reminders");
        Context mContext = getApplicationContext();
        // Refresh widgets data
        BaseActivity.notifyAppWidgets(mContext);

        // Retrieves all notes with reminder set
        DbHelper db = DbHelper.getInstance(mContext);
        List<Note> notes = db.getNotesWithReminder(true);
        Ln.d("Found " + notes.size() + " reminders");
        for (Note note : notes) {
            ReminderHelper.addReminder(OmniNotes.getAppContext(), note);
        }
        return Service.START_NOT_STICKY;
    }

}
