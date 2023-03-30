package mypro.db1;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GenericTableAccess<T> {

    private Connection connection;

    private Class theclass;

    private String table_name;

    public GenericTableAccess(String db, Class theclass) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection(db);
        this.theclass = theclass;

        for(var field: theclass.getDeclaredFields()) {
            System.out.println(field.getName());

            DBFieldTableName dbFieldTableName = field.getAnnotation(DBFieldTableName.class);
            if (dbFieldTableName != null) {
                table_name = dbFieldTableName.table_name();
            }
        }
    }

    public ArrayList<T> readFromTable() throws ClassNotFoundException,
            InstantiationException, IllegalAccessException {

        ArrayList<T> result = new ArrayList<>();

        try
        {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("select * from " + table_name);

            HashMap<Class, IGetField> getFromDB = new HashMap<>();
            getFromDB.put(int.class, (String column) -> rs.getInt(column));
            getFromDB.put(String.class, (String column) -> rs.getString(column));

            while (rs.next()) {

                Constructor<T> ctors = theclass.getDeclaredConstructor();
                T item = ctors.newInstance();

                for(var field: theclass.getDeclaredFields()) {
                    System.out.println(field.getName());

                    DBField dbField = field.getAnnotation(DBField.class);
                    if (dbField != null) {
                        System.out.println(dbField);
                        System.out.println("column_name = " + dbField.column_name());
                        System.out.println("type = " + dbField.type());
                        System.out.println("pk = " + dbField.isPrimaryKey());

                        //int id = rs.getInt("id");
                        //String name = rs.getString("name");

//                        if (dbField.type() == int.class)
//                            field.set(item, rs.getInt(dbField.column_name()));
//                        else if (dbField.type() == String.class)
//                            field.set(item, rs.getString(dbField.column_name()));
                        field.set(item,
                                getFromDB.get(dbField.type()).
                                        getData(dbField.column_name()));
                    }
                }

                result.add(item);
            }
        }
        catch (SQLException | InvocationTargetException | NoSuchMethodException e) {
            System.out.println(e);
        }

        return result;
    }
    public void updateTable(T item, int id) {
        "UPDATE PERSON SET [column_name] = [value]"
    }

    public void createTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        DBField primaryKeyDBField = null;
        for(var field: theclass.getDeclaredFields()) {
            DBField dbField = field.getAnnotation(DBField.class);
            if (dbField != null) {
                if (dbField.isPrimaryKey()) {
                    System.out.println("found primary key");
                    primaryKeyDBField = dbField;
                    break;
                }
            }
        }

        HashMap<Class, String> mapJavaFieldToSql = new HashMap<>();
        mapJavaFieldToSql.put(int.class, "integer");
        mapJavaFieldToSql.put(String.class, "string");


        //statement.executeUpdate("create table person (id PRIMARY KEY integer, name string)");
        String query = "create table " + table_name + " (";
        if (primaryKeyDBField != null) {
            query += primaryKeyDBField.column_name() + " PRIMARY KEY " +
                    mapJavaFieldToSql.get(primaryKeyDBField.type()) + ",";
        }

        for(var field: theclass.getDeclaredFields()) {
                            // field.name == name
            //field.get(item) // item -- (Person) : {name:'danny', id: 1}
            DBField dbField = field.getAnnotation(DBField.class);
            if (dbField != null && dbField != primaryKeyDBField) {
                query += dbField.column_name() + " " + mapJavaFieldToSql.get(dbField.type()) + ",";
            }
        }
        // remove last comma
        query += ")";

        statement.executeUpdate(query);

        // INSERRT -- insert into person values(1, 'leo')
        // if type == String ->   ' + value + '
    }

}
