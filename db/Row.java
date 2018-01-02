package db;

import java.util.ArrayList;

import static db.ColumnSpec.mergeColumnSpec;

public class Row {
    ColumnSpec columnSpec;
    Item[] items;
//    public static void main(String[] args){
//        ArrayList<Column> columns1 = new ArrayList<>();
//        ArrayList<Column> columns2 = new ArrayList<>();
//        columns1.add(new Column("x",Type.FLOAT));
//        columns1.add(new Column("y",Type.FLOAT));
//        columns2.add(new Column("g",Type.FLOAT));
//        columns2.add(new Column("z",Type.FLOAT));
//        ColumnSpec spec1 = new ColumnSpec(columns1);
//        ColumnSpec spec2 = new ColumnSpec(columns2);
//        Item[] arr1 = new Item[2];
//        Item[] arr2 = new Item[2];
//        arr1[0] = new Item("5",Type.FLOAT);
//        arr1[1] = new Item("6",Type.FLOAT);
//        arr2[0] = new Item("5",Type.FLOAT);
//        arr2[1] = new Item("7",Type.FLOAT);
//        Row r1 = new Row(arr1,spec1);
//        Row r2 = new Row(arr2,spec2);
//        System.out.print(r1.rowJoin(r2));
//    }

    public Row(Item[] items,ColumnSpec columnSpec) {
        this.items = items;
        this.columnSpec = columnSpec;

    }

    public Item getItem(int i) {
        return items[i];
    }

    public Item getItem(String query){
        for(int i = 0;i<columnSpec.columns.size();i++) {
            if (columnSpec.columns.get(i).colName.equals(query)) {
                return items[i];
            }
        }
        return null;
    }


    public String toString() {
        Boolean firstElem = true;
        String rowString = "";
        for(Item i: items){
            if(!firstElem){
                rowString += ",";
            }
            rowString += i.toString();
            firstElem = false;
        }
        return rowString;
    }
    public Row rowJoin(Row r){
        ColumnSpec newColSpec = mergeColumnSpec(this.columnSpec,r.columnSpec);
        Item[] newItemArray = new Item[newColSpec.columns.size()];
        for(int i = 0;i<newColSpec.columns.size();i++){
            Column c = newColSpec.columns.get(i);
            if(this.columnSpec.colExists(c.colName)){
                if(r.columnSpec.colExists(c.colName)){
                    if(this.getItem(c.colName).value.equals(r.getItem(c.colName).value)){
                        newItemArray[i] = new Item(this.getItem(c.colName).value,c.colType);
                    }
                    else{
                        return null;
                    }

                } else {
                    newItemArray[i] = this.getItem(c.colName);
                }
            } else {
                newItemArray[i] = r.getItem(c.colName);
            }
        }
        return new Row(newItemArray,newColSpec);

    }

}
