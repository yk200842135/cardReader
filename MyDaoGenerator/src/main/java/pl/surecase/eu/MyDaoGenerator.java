package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(4, "greendao");
        schema.setDefaultJavaPackageDao("cn.com.reformer.poi.db");
        Entity userBean = schema.addEntity("LoginRecord");
        userBean.setTableName("login_records");
        userBean.addIdProperty();
        userBean.addIntProperty("state");
        userBean.addStringProperty("user");
        userBean.addStringProperty("time");

        new DaoGenerator().generateAll(schema, args[0]);
    }
}
