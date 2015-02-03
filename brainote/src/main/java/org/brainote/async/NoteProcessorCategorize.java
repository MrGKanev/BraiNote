
package org.brainote.async;

import org.brainote.OmniNotes;
import org.brainote.db.DbHelper;
import org.brainote.models.Category;
import org.brainote.models.Note;

import java.util.List;


public class NoteProcessorCategorize extends NoteProcessor {

    Category category;


    public NoteProcessorCategorize(List<Note> notes, Category category) {
        super(notes);
        this.category = category;
    }


    @Override
    protected void processNote(Note note) {
        note.setCategory(category);
        DbHelper.getInstance(OmniNotes.getAppContext()).updateNote(note, false);
    }
}
