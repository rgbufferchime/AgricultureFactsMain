package com.bufferchime.agriculturefact.model;

import java.util.ArrayList;

public class Question {
    public int id;
    private String question, note, level,trueAns,image,ansOption;
    private ArrayList<String> options = new ArrayList<String>();

    public Question() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Question(String question) {
        super();
        this.question = question;
    }

    public String getAnsOption() {
        return ansOption;
    }

    public void setAnsOption(String ansOption) {
        this.ansOption = ansOption;
    }

    public String getQuestion() {
        return question;
    }

    public boolean addOption(String option) {
        return this.options.add(option);
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public String getTrueAns() {
        return trueAns;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTrueAns(String trueAns) {
        this.trueAns = trueAns;
    }

    @Override
    public String toString() {
        return "Question: " + question + " OptionS: " + options;
    }

}
