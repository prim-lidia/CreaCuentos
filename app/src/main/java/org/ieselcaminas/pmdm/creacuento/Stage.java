package org.ieselcaminas.pmdm.creacuento;

import java.util.ArrayList;
import java.util.HashMap;

public class Stage {
    String idStage;
    String tale;
    int number;
    Boolean completed;
    String title;
    String image;
    String text;
    HashMap<String, String> options;
    ArrayList<String> previousStages;

    public Stage(){

    }
    public Stage(String idStage, String tale, int number) {
        this.idStage = idStage;
        this.tale = tale;
        this.number =  number;
    }

    public Stage(String idStage, String tale, int number, String title, String image, String text, HashMap<String, String> options, ArrayList<String> previousStages) {
        this.idStage = idStage;
        this.tale = tale;
        this.number =  number;
        this.title = title;
        this.image = image;
        this.text = text;
        this.options = options;
        this.previousStages = previousStages;
    }

    public String getTale() {
        return tale;
    }

    public void setTale(String tale) {
        this.tale = tale;
    }

    public String getIdStage() {
        return idStage;
    }

    public void setIdStage(String idStage) {
        this.idStage = idStage;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public HashMap<String, String> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, String> options) {
        this.options = options;
    }

    public ArrayList<String> getPreviousStages() {
        return previousStages;
    }

    public void setPreviousStages(ArrayList<String> previousStages) {
        this.previousStages = previousStages;
    }
}
