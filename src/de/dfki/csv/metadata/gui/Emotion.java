package de.dfki.csv.metadata.gui;

import de.dfki.csv.metadata.MetaDataObject;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by alvaro on 2/20/17.
 */
public class Emotion implements MetaDataObject {
    private String data;
    private Label label;

    public Emotion(String data){
        this.data = data;
    }
    @Override
    public void render(Object object) {
        if(object instanceof Label){
            setEmoji((Label) object);
        }
    }

    private void setEmoji(Label object) {
        label = object;
        Image image = getImage();
        /*if(image != null){
            setLabelEmoji(image);
        }*/
    }

    private void setLabelEmoji(Image image) {

        label.setGraphic(new ImageView(image));
    }

    private Image getImage() {
        Image image = null;
        if(data.equalsIgnoreCase("smiling")){
            String text = label.getText() + "   :)";
            label.setText(text);
        }

        return image;
    }
}
