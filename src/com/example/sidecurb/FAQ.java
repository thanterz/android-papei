package com.example.sidecurb;

public class FAQ {
	private String question;
    private String answer;
 
    public FAQ (String question, String answer) {
        super();
        this.question 	= question;
        this.answer 	= answer;
    }

	public CharSequence getQuestion() {
		// TODO Auto-generated method stub
		return this.question;
	}

	public CharSequence getAnswer() {
		// TODO Auto-generated method stub
		return this.answer;
	}
	
	public void setQuestion(String question) {
		// TODO Auto-generated method stub
		this.question		= question;
	}

	public void setAnswer(String answer) {
		// TODO Auto-generated method stub
		this.answer	= answer;
	}
}
