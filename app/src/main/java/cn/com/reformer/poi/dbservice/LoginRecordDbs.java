package cn.com.reformer.poi.dbservice;

import android.content.Context;

import java.util.List;

import cn.com.reformer.poi.db.DaoSession;
import cn.com.reformer.poi.db.LoginRecordDao;
import cn.com.reformer.poi.global.SysApplication;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.query.WhereCondition;
import greendao.LoginRecord;

/**
 * Created by Administrator on 2016-11-10.
 */
public class LoginRecordDbs {
    private static final String TAG = LoginRecordDbs.class.getSimpleName();
    private static LoginRecordDbs instance;
    private DaoSession mDaoSession;
    private LoginRecordDao loginRecordDao;

    private LoginRecordDbs() {
    }

    /**
     * 采用单例模式
     * @param context     上下文
     * @return            dbservice
     */
    public static LoginRecordDbs getInstance(Context context) {
        if (instance == null) {
            instance = new LoginRecordDbs();
            instance.mDaoSession = SysApplication.getDaoSession(context);
            instance.loginRecordDao = instance.mDaoSession.getLoginRecordDao();
        }
        return instance;
    }

    /**
     * 取出所有数据
     * @return      所有数据信息
     */
    public List<LoginRecord> loadAllNote(){
        return loginRecordDao.loadAll();
    }

    public List<LoginRecord> loadAllNoteByDsc(){
        return loginRecordDao.queryBuilder().orderDesc(LoginRecordDao.Properties.Id).list();
    }
    /**
     * 根据查询条件,返回数据列表
     * @param where        条件
     * @param params       参数
     * @return             数据列表
     */
    public List<LoginRecord> queryNote(String where, String... params){
        return loginRecordDao.queryRaw(where, params);
    }

    public List<LoginRecord> queryNote(WhereCondition cond, WhereCondition... condMore){
        return loginRecordDao.queryBuilder().where(cond,condMore).build().list();
    }

    public List<LoginRecord> queryNoteOrderBy(Property properties){
        return loginRecordDao.queryBuilder().orderAsc(properties).list();
    }

    /**
     * 查询数据的总条数
     * @return
     */
    public long queryCount(){
        return loginRecordDao.count();
    }

    /**
     * 根据用户信息,插件或修改信息
     * @param loginRecord              用户信息
     * @return 插件或修改的用户id
     */
    public long saveNote(LoginRecord loginRecord){
        return loginRecordDao.insertOrReplace(loginRecord);
    }

    /**
     * 批量插入或修改用户信息
     * @param list      用户信息列表
     */
    public void saveNoteLists(final List<LoginRecord> list){
        if(list == null || list.isEmpty()){
            return;
        }
        loginRecordDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<list.size(); i++){
                    LoginRecord loginRecord = list.get(i);
                    loginRecordDao.insertOrReplace(loginRecord);
                }
            }
        });
    }

    /**
     * 删除所有数据
     */
    public void deleteAllNote(){
        loginRecordDao.deleteAll();
    }

    /**
     * 根据id,删除数据
     * @param id      用户id
     */
    public void deleteNote(long id){
        loginRecordDao.deleteByKey(id);
    }

    public void deleteNotes(List<LoginRecord> lstPayRecord){
        loginRecordDao.deleteInTx(lstPayRecord);
    }

    /**
     * 根据用户类,删除信息
     * @param loginRecord    用户信息类
     */
    public void deleteNote(LoginRecord loginRecord){
        loginRecordDao.delete(loginRecord);
    }
}
