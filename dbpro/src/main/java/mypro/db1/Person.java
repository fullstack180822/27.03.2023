package mypro.db1;

// DTO
public class Person {

    @DBFieldTableName(table_name =  "person")
    public static String TABLE_NAME = "person";

    public Person() {
    }

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @DBField(column_name = "id", isPrimaryKey = true, type = int.class)
    public int id;

    @DBField(column_name = "name", type = String.class)
    public String name;

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
