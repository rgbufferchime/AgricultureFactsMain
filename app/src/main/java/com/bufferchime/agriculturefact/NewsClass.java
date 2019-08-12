package com.bufferchime.agriculturefact;

public class NewsClass {
    private int position;
    private String title,description,url,date;

    public NewsClass(int position, String title, String description, String url,String date)
    {
        this.setPosition(position);
        this.setName(title);
        this.setTime(description);
        this.setDate(url);
        this.setDate2(date);
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

    public String getDate2() {
        return date;
    }

    public void setDate2(String date) {
        this.date = date;
    }


}