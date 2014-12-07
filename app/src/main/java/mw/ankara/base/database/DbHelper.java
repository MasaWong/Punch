package mw.ankara.base.database;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * 数据库Helper基类，使用数据库的类自行继承该类，提供创建数据库、CRUD数据的功能
 *
 * @author MasaWong
 * @date 14/12/3.
 */
public abstract class DbHelper extends SQLiteOpenHelper {

    protected DbHelper(Context context, String name,
            SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    protected DbHelper(Context context, String name,
            SQLiteDatabase.CursorFactory factory, int version,
            DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    /**
     * 抽象方法，需要自行实现数据库的创建工作
     * 不要使用{@link #getWritableDatabase()}或{@link #getReadableDatabase()}，创建的时候已经在
     * SQLiteDatabase里了，建议使用{@link #create}，封装了表的创建工作，需要传入{@link SQLitable}的实例
     *
     * @param db SQLiteDatabase
     */
    @Override
    public abstract void onCreate(SQLiteDatabase db);


    /**
     * 抽象方法，需要自行实现数据库的创建工作
     * 不要使用{@link #getWritableDatabase()}或{@link #getReadableDatabase()}，创建的时候已经在
     * SQLiteDatabase里了
     * TODO：封装的功能有待实现
     *
     * @param db SQLiteDatabase
     */
    @Override
    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    /**
     * 插入操作
     *
     * @param record 需要插入的记录
     * @return 插入之后的行号，可能有用，暂时先返回，不做处理
     */
    public long insert(SQLitable record) {
        return getWritableDatabase().insert(record.getClass().getSimpleName(),
                null, record.writeToSQLite());
    }

    /**
     * 查询总和，只支持int型的查询
     *
     * @param clazz         数据库类型
     * @param key           需要查询总和的字段
     * @param selectionArgs 查询条件
     * @return 返回总和
     */
    public int querySum(Class<? extends SQLitable> clazz, String key, String[] selectionArgs) {
        String query = "select sum(" + key + ") from " + clazz.getSimpleName();
        Cursor cursor = getReadableDatabase().rawQuery(query, selectionArgs);

        int sum = cursor.moveToNext() ? cursor.getInt(0) : 0;
        cursor.close();
        return sum;
    }

    /**
     * 查询操作
     *
     * @param clazz         传入一个实现了SQLitable的类
     * @param selection     数据库查询where语句
     * @param selectionArgs 数据库查询where条件
     * @param groupBy       数据库分组
     * @param having        数据库条件
     * @param orderBy       数据库排序规则
     * @return 返回一个ArrayList，存储着数据库查询的结果
     */
    public ArrayList<SQLitable> query(Class<? extends SQLitable> clazz, String selection,
            String[] selectionArgs, String groupBy, String having, String orderBy) {
        ArrayList<SQLitable> records = new ArrayList<SQLitable>();

        Cursor cursor = getReadableDatabase().query(clazz.getSimpleName(),
                null, selection, selectionArgs, groupBy, having, orderBy);
        try {
            while (cursor.moveToNext()) {
                SQLitable record = clazz.newInstance();
                record.readFromSQLite(cursor);
                records.add(record);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return records;
    }

    /**
     * 查询操作
     *
     * @param clazz 传入一个实现了SQLitable的类
     * @return 返回一个ArrayList，存储着数据库查询的结果
     */
    public ArrayList<SQLitable> query(Class<? extends SQLitable> clazz) {
        ArrayList<SQLitable> records = new ArrayList<SQLitable>();

        Cursor cursor = getReadableDatabase().query(clazz.getSimpleName(),
                null, null, null, null, null, null);
        try {
            while (cursor.moveToNext()) {
                SQLitable record = clazz.newInstance();
                record.readFromSQLite(cursor);
                records.add(record);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return records;
    }

    /**
     * 更新操作
     *
     * @param record      需要更新的记录
     * @param whereClause sql语句的where条件
     * @param whereArgs   sql语句的where参数
     * @return 被更新的行数
     */
    public int update(SQLitable record, String whereClause, String[] whereArgs) {
        return getWritableDatabase().update(record.getClass().getSimpleName(),
                record.writeToSQLite(), whereClause, whereArgs);
    }

    /**
     * 表的创建工作
     *
     * @param database onCreate里的参数
     * @param table    实现了SQLitable的类的实例
     */
    public void create(SQLiteDatabase database, SQLitable table) {
        database.execSQL(table.getSQLiteCreation());
    }


    /**
     * 表的创建工作
     *
     * @param database onCreate里的参数
     * @param tables   实现了SQLitable的类的数据，直接用类来做不太好做，onCreate基本只执行一次，浪费一些效率还
     *                 能接受，传入实现了SQLitable的类的实例
     */
    public void create(SQLiteDatabase database, SQLitable[] tables) {
        for (SQLitable table : tables) {
            create(database, table);
        }
    }
}
