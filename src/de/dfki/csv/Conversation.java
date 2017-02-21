package de.dfki.csv;

import de.dfki.csv.metadata.MetaDataObject;

import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Created by alvaro on 2/20/17.
 */
public class Conversation {
    private String sessionId;
    private TreeSet messages = new TreeSet();
    public Conversation(String sessionId){
        this.sessionId = sessionId;
    }

    public void add(ConversationLine line){
        getMessages().add(line);
    }

    public void print(){
        for(Object objectLine: getMessages()){
            //String format = "%-20s %5d\n";
            ConversationLine line = (ConversationLine) objectLine;
            System.out.println("User: " + line.getUserQuestion() );
            String response = "System: " + line.getSystemResponse();
            System.out.printf( "%-15s %65s %n", "", response);


        }
    }


    public TreeSet getMessages() {
        return messages;
    }
}
