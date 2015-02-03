
package org.brainote.models.listeners;

import org.brainote.models.Attachment;


public interface OnAttachingFileListener {

    public void onAttachingFileErrorOccurred(Attachment mAttachment);

    public void onAttachingFileFinished(Attachment mAttachment);
}
