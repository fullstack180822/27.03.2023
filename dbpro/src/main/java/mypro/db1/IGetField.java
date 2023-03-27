package mypro.db1;

import java.sql.SQLException;

public interface IGetField<T> {

    T getData(String column_name) throws SQLException;

}
