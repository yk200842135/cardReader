package cn.com.reformer.poi.bean.base;

/**
 * Created by Administrator on 2016-11-02.
 */
public class LoginRecordBean {
    private boolean login;
    private String userName;
    private String date;

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
