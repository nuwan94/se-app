package me.nuwan.seofficial.Model;

public class Login {
    private String sno,name,pwd;
    public static Login currentToken;

    public Login() {
    }

    public Login(String sno, String name, String pwd) {
        this.sno = sno;
        this.name = name;
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
