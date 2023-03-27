package mypro.db1;

import java.lang.annotation.*;
@Documented
@Target({ElementType.FIELD})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface DBFieldTableName {
    String table_name();
}