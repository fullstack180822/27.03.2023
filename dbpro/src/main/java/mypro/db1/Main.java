package mypro.db1;

import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {


        DBAccess dbAccess = new DBAccess();

        //ArrayList<Person> people = dbAccess.readFromPerson();
        //ArrayList<Person> people = dbAccess.readFromTable(Person.TABLE_NAME);

        //GenericTableAccess<Person> personDAO = new GenericTableAccess<>("jdbc:sqlite:D:\\databases\\Sqlite\\company.db");



        //System.out.println(people);

        Person p = new Person(1, "danny");
        // meta-data

        GenericTableAccess<Person> genericTableAccess = new GenericTableAccess<>(
                "jdbc:sqlite:D:\\databases\\Sqlite\\company.db",
                Person.class);
        var result = genericTableAccess.readFromTable();
        System.out.println(result);


    }

}
