package com.bufferchime.agriculturefact.materialdesgin;

public class QuizListItem {
	private int imageId;
	private String color;
	private String firstchar;
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getQuizTitle() {
		return quizTitle;
	}

	public void setQuizTitle(String quizTitle) {
		this.quizTitle = quizTitle;
	}

	private String quizTitle;

	public QuizListItem( int imageId, String quizTitle) {
		this.imageId=imageId;
		this.quizTitle=quizTitle;
	}
	public QuizListItem(int imageId, String quizTitle, String color) {
		this.imageId=imageId;
		this.quizTitle=quizTitle;
		this.color=color;
	}
	
	public QuizListItem(int imageId, String quizTitle, String color, String firstchar) {
		this.imageId=imageId;
		this.quizTitle=quizTitle;
		this.color=color;
		this.firstchar=firstchar;
	}

	public String getFirstchar() {
		return firstchar;
	}

	public void setFirstchar(String firstchar) {
		this.firstchar = firstchar;
	}
}