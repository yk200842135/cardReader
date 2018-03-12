package cn.com.reformer.poi.bean.response;

/**
 * Created by Administrator on 2017-09-19.
 */

public class ResponseEmplyMsg extends ResponseBase {
    private String emplyName;
    private String cardNum;
    private String departName;
    private String photoPath;
    private String emplyType;
    private String emplyId;

    public String getEmplyName() {
        return emplyName;
    }

    public void setEmplyName(String emplyName) {
        this.emplyName = emplyName;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getEmplyType() {
        return emplyType;
    }

    public void setEmplyType(String emplyType) {
        this.emplyType = emplyType;
    }

    public String getEmplyId() {
        return emplyId;
    }

    public void setEmplyId(String emplyId) {
        this.emplyId = emplyId;
    }
}
