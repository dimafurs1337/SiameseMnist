package com.example.siamesemnist;

public class HistoryItem {
    private int id;
    private String selectedNum;
    private String score;
    private byte[] image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSelectedNum() {
        return selectedNum;
    }

    public void setSelectedNum(String selectedNum) {
        this.selectedNum = selectedNum;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public HistoryItem(int id, String selectedNum, String score, byte[] image) {
        this.id = id;
        this.selectedNum = selectedNum;
        this.score = score;
        this.image = image;
    }




}
