package de.dfki.csv.metadata;

import de.dfki.csv.metadata.gui.Emotion;
import de.dfki.csv.metadata.gui.ImageLink;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.LinkedList;

/**
 * Created by alvaro on 2/20/17.
 */
public class Metadata {
    private String data;
    private JSONArray parsedData;
    private LinkedList<MetaDataObject> metaDataObjects = new LinkedList<>();

    public Metadata(String value){
        data = value;
        parse();
    }

    public LinkedList<MetaDataObject> parse(){
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(data);
            parsedData = (JSONArray)obj;
            createMetaObjects();
        } catch (ParseException e) {
            DummyMetaObject d = new DummyMetaObject();
            metaDataObjects.add(d);
        }
        return metaDataObjects;

    }

    private void createMetaObjects() {
        for(int i = 0; i < parsedData.size(); i++){
            JSONObject object = (JSONObject) parsedData.get(i);
            String key = (String) object.get("key");
            String value = (String) object.get("value");
            MetaDataObject obj = makeObject(key, value);
            metaDataObjects.add(obj);
        }
    }

    private MetaDataObject makeObject(String key, String value) {
        if(key.equalsIgnoreCase("emotion")){
            Emotion emotion = new Emotion(value);
            return emotion;
        }else if(key.equalsIgnoreCase("imagelink")){
            ImageLink imageLink = new ImageLink(value);
            return imageLink;
        }else {
            return new DummyMetaObject();
        }

    }
}
