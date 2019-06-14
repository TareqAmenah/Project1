package com.amenah.tareq.project1;

import com.amenah.tareq.project1.ConnectionManager.Messages.Message;

public interface ChatActivityControler {
    public void addTextMessageToLayout(Message message);

    public void addImageMessageToLayout(Message message);

    public void addBinaryFileMessageToLayout(Message message);
}
