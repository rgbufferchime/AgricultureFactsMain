package com.bufferchime.agriculturefact.Notespack;

public class note{
    private int position;
    private String name,link;

    public note(int position, String name, String link)
    {
        this.setPosition(position);
        this.setName(name);
        this.setLink(link);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }




}