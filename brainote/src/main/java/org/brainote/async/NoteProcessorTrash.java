
package org.brainote.async;

import org.brainote.OmniNotes;
import org.brainote.db.DbHelper;
import org.brainote.models.Note;
import org.brainote.utils.ReminderHelper;
import org.brainote.utils.ShortcutHelper;

import java.util.List;


public class NoteProcessorTrash extends NoteProcessor {

    boolean trash;


    public NoteProcessorTrash(List<Note> notes, boolean trash) {
        super(notes);
        this.trash = trash;
    }


    @Override
    protected void processNote(Note note) {
        if (trash) {
            ShortcutHelper.removeshortCut(OmniNotes.getAppContext(), note);
            ReminderHelper.removeReminder(OmniNotes.getAppContext(), note);
        } else {
            ReminderHelper.addReminder(OmniNotes.getAppContext(), note);
        }
        DbHelper.getInstance(OmniNotes.getAppContext()).trashNote(note, trash);
    }
}
