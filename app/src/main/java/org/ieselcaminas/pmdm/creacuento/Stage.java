package org.ieselcaminas.pmdm.creacuento;

import java.util.ArrayList;
import java.util.HashMap;

public class Stage {
    String title;
    String image;
    String text;
    HashMap<String, String> options;
    ArrayList<String> previousStages;

    public Stage(String title, String prev){
        this.title = title;
        this.image = null;
        this.text = null;
        this.options = null;
        this.previousStages =  new ArrayList<>();
        previousStages.add(prev);
    }
    public Stage(String title, String image, String text, HashMap<String, String> options, ArrayList<String> previousStages) {
        this.title = title;
        this.image = image;
        this.text = text;
        this.options = options;
        this.previousStages = previousStages;
    }

}
