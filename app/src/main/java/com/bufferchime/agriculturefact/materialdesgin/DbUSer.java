package com.bufferchime.agriculturefact.materialdesgin;

public class DbUSer {

	private String quizName;
	private String quizlevelname;
	private int totalNoQuestions;
	private int score;
	private String dateofquiz;
	private int attempts;
	private String firstchar;
	private String color;
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}


	public int getAttempts() {
		return attempts;
	}


	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}


	public String getQuizName() {
		return quizName;
	}


	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}


	public String getQuizlevelname() {
		return quizlevelname;
	}


	public void setQuizlevelname(String quizlevelname) {
		this.quizlevelname = quizlevelname;
	}


	public int getTotalNoQuestions() {
		return totalNoQuestions;
	}


	public void setTotalNoQuestions(int totalNoQuestions) {
		this.totalNoQuestions = totalNoQuestions;
	}


	public int getScore() {
		return score;
	}


	public void setScore(int score) {
		this.score = score;
	}


	public String getDateofquiz() {
		return dateofquiz;
	}


	public void setDateofquiz(String dateofquiz) {
		this.dateofquiz = dateofquiz;
	}


	// constructors
	public DbUSer() {
	}
	public String getFirstchar() {
		return firstchar;
	}

	public void setFirstchar(String firstchar) {
		this.firstchar = firstchar;
	}

}