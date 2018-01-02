package db;

import java.util.ArrayList;

/**
 * Created by jessicahsiao on 3/5/17.
 */
public class ColumnSpec {

    //    public static void main(String[] args){
//        ArrayList<Column> columns1 = new ArrayList<>();
//        ArrayList<Column> columns2 = new ArrayList<>();
//        columns1.add(new Column("x",Type.FLOAT));
//        columns1.add(new Column("y",Type.FLOAT));
//        columns2.add(new Column("x",Type.FLOAT));
//        columns2.add(new Column("z",Type.FLOAT));
//        ColumnSpec spec1 = new ColumnSpec(columns1);
//        ColumnSpec spec2 = new ColumnSpec(columns2);
//        System.out.print(mergeColumnSpec(spec1,spec2));
//    }
    ArrayList<Column> columns;

    public ColumnSpec(String[] colNames, Type[] colTypes) {
        columns = new ArrayList<>();
        for (int i = 0; i < colNames.length; i++) {
            this.columns.add(new Column(colNames[i], colTypes[i]));
        }
    }

    public ColumnSpec() {
        this.columns = new ArrayList<>();

    }

    public ColumnSpec(ArrayList<Column> columns) {

        this.columns = columns;
    }

    public void addColumn(Column c) {
        this.columns.add(c);
    }

    public static ColumnSpec mergeColumnSpec(ColumnSpec c1, ColumnSpec c2) {
        ArrayList<Column> returnValList = new ArrayList<>();
        ColumnSpec returnVal = new ColumnSpec(returnValList);
        for (int i = 0; i < c1.columns.size(); i++) {
            for (int j = 0; j < c2.columns.size(); j++) {
                if (c1.columns.get(i).colName.equals(c2.columns.get(j).colName)) {
                    returnVal.addColumn(c1.columns.get(i));
                }
            }
        }
        for (Column c : c1.columns) {
            if (!returnVal.colExists(c.colName)) {
                returnVal.addColumn(c);
            }
        }
        for (Column c : c2.columns) {
            if (!returnVal.colExists(c.colName)) {
                returnVal.addColumn(c);
            }
        }
        //String colSpecString = returnVal.toString();
        //System.out.println(colSpecString);
        return returnVal;

    }

    public boolean colExists(String s) {
        for (Column c : columns) {
            if (c.colName.equals(s)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        String returnVal = "";
        Boolean firstElem = true;
        for (Column c : columns) {
            if (!firstElem) {
                returnVal += ",";
            }
            returnVal += c.colName + " " + c.colType.toString().toLowerCase();
            firstElem = false;
        }
        return returnVal;
    }

}
