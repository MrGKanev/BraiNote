
package org.brainote.async;

import org.brainote.OmniNotes;
import org.brainote.db.DbHelper;
import org.brainote.models.Note;

import java.util.List;


public class NoteProcessorArchive extends NoteProcessor {

    boolean archive;


    public NoteProcessorArchive(List<Note> notes, boolean archive) {
        super(notes);
        this.archive = archive;
    }


    @Override
    protected void processNote(Note note) {
        DbHelper.getInstance(OmniNotes.getAppContext()).archiveNote(note, archive);
    }
}
