package com.egame.bilgiyarismasi;
//Soru sınıfı
public class Question {
    String question,a,b,c,d,ans;

    Question()
    {

    }
    public Question(String question, String a, String b, String c, String d, String ans)
    {
        this.question = question;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.ans = ans;
    }
    public String getQuestion() {
        return question;
    }

    public String getA() {
        return a;
    }

    public String getB() {
        return b;
    }

    public String getC() {
        return c;
    }

    public String getD() {
        return d;
    }

    public String getAns() {
        return ans;
    }
}
