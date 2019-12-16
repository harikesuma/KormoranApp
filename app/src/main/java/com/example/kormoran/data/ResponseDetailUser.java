package com.example.kormoran.data;

public class ResponseDetailUser {

    String msg;
    String answer;
    String question;
    int like;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    @Override
    public String toString(){
        return
                "ResponseDetailQuestion{" +
                        ",msg = '" + msg + '\'' +
                        ",answer = '" + answer + '\'' +
                        ",question = '" + question + '\'' +
                        ",like = '" + like + '\'' +
                        "}";
    }
}
