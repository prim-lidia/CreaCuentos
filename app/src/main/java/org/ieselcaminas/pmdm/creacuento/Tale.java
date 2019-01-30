package org.ieselcaminas.pmdm.creacuento;

import java.io.Serializable;
import java.util.ArrayList;

public class Tale implements Serializable {
    public String creator;
    private String title;
    private String author;
    private String illustrationAuthor;
    private String description;
    private String frontImage;
    private String category;
    private Boolean editing;
    private ArrayList<Stage> stages;

    public Tale(String title, String author, String illustrationAuthor, String description, String frontImage, String category){
        this.title = title;
        this.author = author;
        this.illustrationAuthor = illustrationAuthor;
        this.description = description;
        this.frontImage =  frontImage;
        this.category = category;
        //this.stages = stages;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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
