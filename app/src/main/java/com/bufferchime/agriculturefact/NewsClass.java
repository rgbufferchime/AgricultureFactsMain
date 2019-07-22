package com.bufferchime.agriculturefact;

public class NewsClass {
    private int position;
    private String title,description,url;

    public NewsClass(int position, String title, String description, String url)
    {
        this.setPosition(position);
        this.setName(title);
        this.setTime(description);
        this.setDate(url);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return title;
    }

    public void setName(String title) {
        this.title = title;
    }


    public String getTime() {
        return description;
    }

    public void setTime(String description) {
        this.description = description;
    }

    public String getDate() {
        return url;
    }

    public void setDate(String url) {
        this.url = url;
    }


}