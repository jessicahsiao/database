package db;

public class Column {

    String colName;
    Type colType;

    public Column(String name, Type type) {
        colName = name;
        this.colType = type;
    }

}
