package de.dfki.csv.metadata;

/**
 * Created by alvaro on 2/20/17.
 */
public class DummyMetaObject implements MetaDataObject {
    @Override
    public void render(Object object) {
        System.out.println("Do nothing here");
    }
}
