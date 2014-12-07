package mw.ankara.base.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.Field;

/**
 * 支持插入数据库的数据类型，实现了{@link mw.ankara.base.database.SQLitable}接口
 * 利用反射读写数据，考虑到牺牲了一定的效率，应在不需要考虑效率的地方使用
 * 对于效率要求较为严格的地方，使用SQLitable，自定义读写规则
 *
 * @author MasaWong
 * @date 14/12/5.
 */
public class SQLiteRecord implements SQLitable {
    /**
     * 使用反射实现从Cursor中读取数据的方法
     *
     * @param cursor 查询数据库得到的游标
     */
    @Override
    public void readFromSQLite(Cursor cursor) {
        Field[] fields = getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                MapKey mapKey = field.getAnnotation(MapKey.class);

                if (mapKey != null) {
                    field.setAccessible(true);

                    int index = cursor.getColumnIndex(mapKey.key());
                    Class<?> fieldClass = field.getType();
                    // 只转换几种常见类型
                    if (fieldClass == int.class) {
                        field.set(this, cursor.getInt(index));
                    } else if (fieldClass == double.class) {
                        field.set(this, cursor.getDouble(index));
                    } else if (fieldClass == long.class) {
                        field.set(this, cursor.getLong(index));
                    } else if (fieldClass == short.class) {
                        field.set(this, cursor.getShort(index));
                    } else if (fieldClass == float.class) {
                        field.set(this, cursor.getFloat(index));
                    } else if (fieldClass == String.class) {
                        field.set(this, cursor.getString(index));
                    }

                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用反射实现将数据插入到ContentValues的方法
     *
     * @return 数据转换后的结果
     */
    @Override
    public ContentValues writeToSQLite() {
        try {
            ContentValues pairs = new ContentValues();

            Field[] fields = getClass().getDeclaredFields();
            for (Field field : fields) {
                MapKey mapKey = field.getAnnotation(MapKey.class);

                if (mapKey != null) {
                    field.setAccessible(true);
                    Object value = field.get(this);

                    // 只转换几种常见类型
                    if (value instanceof Integer) {
                        pairs.put(mapKey.key(), (Integer) value);
                    } else if (value instanceof Double) {
                        pairs.put(mapKey.key(), (Double) value);
                    } else if (value instanceof Long) {
                        pairs.put(mapKey.key(), (Long) value);
                    } else if (value instanceof Float) {
                        pairs.put(mapKey.key(), (Float) value);
                    } else if (value instanceof Short) {
                        pairs.put(mapKey.key(), (Short) value);
                    } else if (value instanceof String) {
                        pairs.put(mapKey.key(), (String) value);
                    }

                    field.setAccessible(false);
                }
            }

            return pairs;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用反射实现创建表的操作
     *
     * @return 数据库中表创建的执行语句
     */
    @Override
    public String getSQLiteCreation() {
        try {
            boolean foundPrimary = false;
            String creation = "create table if not exists " + getClass().getSimpleName() + " (";

            Field[] fields = getClass().getDeclaredFields();
            for (Field field : fields) {
                MapKey mapKey = field.getAnnotation(MapKey.class);

                if (mapKey != null) {
                    Class<?> fieldClass = field.getType();

                    // 只转换几种常见类型
                    String type;
                    if (fieldClass == int.class || fieldClass == long.class) {
                        type = " integer";
                    } else if (fieldClass == double.class) {
                        type = " double";
                    } else if (fieldClass == short.class) {
                        type = " smallint";
                    } else if (fieldClass == float.class) {
                        type = " float";
                    } else {
                        type = " text";
                    }

                    String primary = "";
                    if (checkPrimary(mapKey, foundPrimary)) {
                        foundPrimary = true;
                        primary = " primary key ";
                    }
                    creation += mapKey.key() + type + primary + ",";
                }
            }
            creation = creation.substring(0, creation.length() - 1) + ")";
            return creation;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 数据库结构不要太复杂，只支持一个主键的形式
     *
     * @param mapKey       标识是否是主键的标记
     * @param foundPrimary 是否已经有主键了
     * @return 该字段是否是主键
     * @throws Exception 如果多于一个主键，则抛错
     */
    protected boolean checkPrimary(MapKey mapKey, boolean foundPrimary) throws Exception {
        if (!mapKey.primary()) {
            return false;
        } else if (foundPrimary) {
            throw new Exception("Too many primary keys");
        } else {
            return true;
        }
    }
}
