package mw.ankara.base.database;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库标记，包含一个String，表示字段名；一个boolean，表示是否是主键
 * 考虑到可能会和JSON公用Annotation，所以取名通用了一点
 *
 * @author MasaWong
 * @date 14/12/5.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Map {
    public String key() default "";

    public boolean primary() default false;
}
