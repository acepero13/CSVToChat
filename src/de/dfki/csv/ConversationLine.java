package de.dfki.csv;

import de.dfki.csv.metadata.MetaDataObject;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by alvaro on 2/20/17.
 */
public class ConversationLine implements Comparable {
    private String session_id;
    private String userQuestion;
    private String systemResponse;
    private Timestamp timestamp;
    private Date date;
    private String emotion;
    private LinkedList<MetaDataObject> metadataObjects;

    public ConversationLine(){

    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getUserQuestion() {
        return userQuestion;
    }

    public void setUserQuestion(String userQuestion) {
        this.userQuestion = userQuestion;
    }

    public String getSystemResponse() {
        return systemResponse;
    }

    public void setSystemResponse(String systemResponse) {
        this.systemResponse = systemResponse;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }


    @Override
    public int compareTo(Object o) {
        ConversationLine line = (ConversationLine)o;
        if(this.timestamp.after(line.getTimestamp())){ // than object
            return 1;
        }else if (this.timestamp.before(line.getTimestamp())){
            return  -1;
        }else {
            return 0;
        }
    }

    public void setMetadataObjects(LinkedList<MetaDataObject> metadataObjects) {
        this.metadataObjects = metadataObjects;
    }

    public LinkedList<MetaDataObject> getMetadataObjects() {
        return metadataObjects;
    }
}
