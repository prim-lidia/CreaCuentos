package org.ieselcaminas.pmdm.creacuento;

import java.io.Serializable;
import java.util.ArrayList;

public class Tale implements Serializable {
    private String idTale;
    private String creator;
    private String title;
    private String author;
    private String illustrationAuthor;
    private String description;
    private String frontImage;
    private int category;
    private Boolean editing;
    private ArrayList<Stage> stages;

    public Tale () {
        this.category = -1;
        this.editing = true;
        this.stages = new ArrayList<Stage>();
    }
    public Tale (String idTale, String creator) {
        this.idTale = idTale;
        this.creator = creator;
        this.category = -1;
        this.editing = true;
        this.stages = new ArrayList<Stage>();
    }

    public Tale(String idTale, String title, String author, String illustrationAuthor, String description, String frontImage, int category){
        this.idTale = idTale;
        this.title = title;
        this.author = author;
        this.illustrationAuthor = illustrationAuthor;
        this.description = description;
        this.frontImage =  frontImage;
        this.category = category;
        this.stages = new ArrayList<Stage>();
    }

    public String getId() {
        return idTale;
    }

    public void setId(String idTale) {
        this.idTale = idTale;
    }


    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIllustrationAuthor() {
        return illustrationAuthor;
    }

    public void setIllustrationAuthor(String illustrationAuthor) {
        this.illustrationAuthor = illustrationAuthor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(String frontImage) {
        this.frontImage = frontImage;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public Boolean getEditing() {
        return editing;
    }

    public void setEditing(Boolean editing) {
        this.editing = editing;
    }

    public ArrayList<Stage> getStages() {
        return stages;
    }

    public void setStages(ArrayList<Stage> stages) {
        this.stages = stages;
    }


}
