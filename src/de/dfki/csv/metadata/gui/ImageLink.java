package de.dfki.csv.metadata.gui;

import de.dfki.csv.metadata.DummyMetaObject;
import de.dfki.csv.metadata.MetaDataObject;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by alvaro on 2/20/17.
 */
public class ImageLink  implements MetaDataObject {
    private  String data;

    public ImageLink(String value) {
        this.data = value;
    }

    @Override
    public void render(Object object) {
        if(object instanceof Label){
            setImage((Label) object);
        }
    }

    private void setImage(Label label) {
        label.setContentDisplay(ContentDisplay.TOP);
        Image image = null;
        JSONParser parser = new JSONParser();
        data = data.replaceAll("``", "\"");
        Object obj = null;
        JSONArray parsedData =null;
        try {
            obj = parser.parse(data);
            if(((JSONObject) obj).containsKey("fotoKlein")){
                String foto = (String) ((JSONObject) obj).get("fotoKlein");
                ImageView imageView = ImageViewBuilder.create().image(new Image(foto)).build();
                label.setGraphic(imageView);
            }

        } catch (ParseException e) {

        }


    }
}
