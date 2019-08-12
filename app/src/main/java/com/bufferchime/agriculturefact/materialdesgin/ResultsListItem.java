package com.bufferchime.agriculturefact.materialdesgin;

public class ResultsListItem {
	private String question;
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUseranswer() {
		return useranswer;
	}
	public void setUseranswer(String useranswer) {
		this.useranswer = useranswer;
	}
	private String answer;
	private String description;
	private String useranswer;
	public ResultsListItem(String question, String answer, String useranswer, String description) {
		this.question = question;
		this.answer = answer;
		this.description = description;
		this.useranswer = useranswer;
	}
	
}