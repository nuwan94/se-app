package me.nuwan.seofficial.Model;

public class Login {
    private String sno,pwd;
    public static Login currentToken;

    public Login() {
    }

    public Login(String sno, String pwd) {
        this.sno = sno;
        this.pwd = pwd;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
