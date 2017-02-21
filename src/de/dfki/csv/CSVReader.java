package de.dfki.csv;

import de.dfki.csv.metadata.MetaDataObject;
import de.dfki.csv.metadata.Metadata;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by alvaro on 2/20/17.
 */
public class CSVReader {
    private String filename ="";
    private CSVParser records;
    private HashMap<String, Conversation> conversations = new HashMap<>();
    char delimiter;
    public CSVReader(String filename, char delimiter){
        this.filename = filename;
        this.delimiter = delimiter;
    }

    public void readFile(){
        try {
            CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(this.delimiter);
            InputStreamReader stream = new InputStreamReader(new FileInputStream(this.filename), "UTF-8");
            records = new CSVParser(stream, format);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Conversation> parse(){
        for (CSVRecord record : records) {
            ConversationLine row = new ConversationLine();
            Record record1 = new Record(record, row).invoke();
            String timestamputc = record1.getTimestamputc();
            String session_id = record1.getSession_id();
            setDate(row, timestamputc);
            addMessageToConversation(row, session_id);

        }
        return conversations;
    }

    private void addMessageToConversation(ConversationLine row, String session_id) {
        if(conversations.containsKey(session_id)){
            Conversation c = conversations.get(session_id);
            c.add(row);
        }else{
            Conversation c = new Conversation(session_id);
            c.add(row);
            conversations.put(session_id, c);
        }
    }

    private void setDate(ConversationLine row, String timestamputc) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = formatter.parse(timestamputc);
            row.setDate(date);
            Timestamp timestamp = new Timestamp(date.getTime());
            row.setTimestamp(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private class Record {
        private CSVRecord record;
        private ConversationLine row;
        private String session_id;
        private String timestamputc;
        private String emotion;

        public Record(CSVRecord record, ConversationLine row) {
            this.record = record;
            this.row = row;
        }

        public String getSession_id() {
            return session_id;
        }

        public String getTimestamputc() {
            return timestamputc;
        }

        public Record invoke() {
            session_id = record.get("sessionuuid");
            timestamputc = record.get("timestamputc");
            String interactionvalue = record.get("interactionvalue");
            String outputtext = record.get("outputtext");
            String safeOutputText = Jsoup.clean(outputtext, Whitelist.simpleText());
            String safeUserText = Jsoup.clean(interactionvalue, Whitelist.simpleText());
            String emotion = record.get("outputmetadata");
            Metadata metadata = new Metadata(emotion);
            LinkedList<MetaDataObject> metaDataObjects = metadata.parse();
            row.setMetadataObjects(metaDataObjects);
            row.setSession_id(session_id);
            row.setUserQuestion(safeUserText);
            row.setSystemResponse(safeOutputText);
            return this;
        }
    }
}
