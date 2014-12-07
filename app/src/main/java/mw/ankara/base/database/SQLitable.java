package mw.ankara.base.database;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * 支持插入数据库的数据类型接口，包含两个方法：
 * 1、从数据库Cursor中读取数据{@link #readFromSQLite(android.database.Cursor)}
 * 2、将数据转换成ContentValues{@link #writeToSQLite()}
 *
 * @author MasaWong
 * @date 14/12/5.
 */
public interface SQLitable {
    /**
     * 自定义实现从Cursor中读取数据的方法
     *
     * @param cursor 查询数据库得到的游标
     */
    public void readFromSQLite(Cursor cursor);

    /**
     * 自定义实现将数据插入到ContentValues的方法
     *
     * @return 数据转换后的结果
     */
    public ContentValues writeToSQLite();

    /**
     * 自定义实现创建表的操作
     *
     * @return 数据库中表创建的执行语句
     */
    public String getSQLiteCreation();
}
