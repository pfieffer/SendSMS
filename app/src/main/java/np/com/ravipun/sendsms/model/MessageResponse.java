package np.com.ravipun.sendsms.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ravi on 2/14/2018.
 */

public class MessageResponse {

    @SerializedName("message-count")
    private String messageCount;

    @SerializedName("messages")
    private Message[] messages;

    public String getMessageCount ()
    {
        return messageCount;
    }

    public void setMessageCount (String messageCount)
    {
        this.messageCount = messageCount;
    }

    public Message[] getMessages ()
    {
        return messages;
    }

    public void setMessages (Message[] messages)
    {
        this.messages = messages;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message-count = "+messageCount+", messages = "+messages+"]";
    }
}
