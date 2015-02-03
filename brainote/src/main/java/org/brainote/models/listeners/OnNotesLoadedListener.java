
package org.brainote.models.listeners;

import org.brainote.models.Note;

import java.util.ArrayList;


public interface OnNotesLoadedListener {

    public void onNotesLoaded(ArrayList<Note> notes);
}
