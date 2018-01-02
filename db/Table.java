package db;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Table {

    public ArrayList<Row> tableRows;
    ColumnSpec columnSpec;

    public Table(ColumnSpec columnSpec) {
        tableRows = new ArrayList<>();
        this.columnSpec = columnSpec;
    }

    public Table(Column c) {
        tableRows = new ArrayList<>();
        this.columnSpec = new ColumnSpec();
        this.columnSpec.addColumn(c);
    }

    //cartesian joins two tables
    public Table TableJoin(Table t) {
        ColumnSpec colSpec = ColumnSpec.mergeColumnSpec(this.columnSpec, t.columnSpec);
        Table returnVal = new Table(colSpec);
        for (Row r1 : this.tableRows) {
            for (Row r2 : t.tableRows) {
                Row r3 = r1.rowJoin(r2);
                if (r3 != null) {
                    returnVal.addRow(r3);
                }
            }
        }
        return returnVal;

    }

    public static Table Join(Table... tables) {
        if (tables.length == 1) {
            return tables[0];
        } else {
            Table returnVal = tables[0];
            for (int i = 1; i < tables.length; i++) {
                returnVal = returnVal.TableJoin(tables[i]);
            }
            return returnVal;
        }
    }

    public void addRowColTable(Item i) {
        Item[] ItemArray = new Item[1];
        ItemArray[0] = i;
        this.addRow(new Row(ItemArray, this.columnSpec));
    }

    public void addRow(Row row) {
        this.tableRows.add(row);
    }

    public void addRow(String[] rowVals) {
        Item[] rowItems = new Item[rowVals.length];
        try {
            for (int i = 0; i < rowVals.length; i++) {
                Column c = this.columnSpec.columns.get(i);
                String val = rowVals[i];
                if (c.colType.equals(Type.type(val))) {
                    rowItems[i] = new Item(val, c.colType);
                } else {
                    throw new AddRowException("Row Table mismatch");
                }
            }
            Row r = new Row(rowItems, this.columnSpec);
            this.addRow(r);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AddRowException("Row Table mismatch");
        }
    }

    public String toString() {
        StringBuilder bob = new StringBuilder(columnSpec.toString());
        //String returnVal = "";
        //returnVal += columnSpec.toString();
        for (Row r : this.tableRows) {
            bob.append("\n");
            bob.append(r);
           // returnVal += "\n" + r.toString();
        }
        return bob.toString();
    }

    public Table combineCols(Table t1, Table t2, String name, Operator operator) {
        Table returnVal = new Table(new Column(name, Type.mergeTypes(t1.columnSpec.columns.get(0).colType, t2.columnSpec.columns.get(0).colType)));
        for (int i = 0; i < t1.tableRows.size(); i++) {
            returnVal.addRowColTable(operator.evaluate(t1.tableRows.get(i).getItem(0), t2.tableRows.get(i).getItem(0)));
        }
        return returnVal;
    }

    public Table combineCols(Table t1, Item i1, String name, Operator operator) {
        Table returnVal = new Table(new Column(name, Type.mergeTypes(t1.columnSpec.columns.get(0).colType, i1.type)));
        for (int i = 0; i < t1.tableRows.size(); i++) {
            returnVal.addRowColTable(operator.evaluate(t1.tableRows.get(i).getItem(0), i1));
        }
        return returnVal;
    }

    public Table evalColExpr(String expr) {
        if (expr.split("\\s+as\\s+").length == 1) {
            return getColumn(expr.trim());
        } else {
            String colName = expr.trim().split("\\s+as\\s+")[1];
            expr = expr.trim().split("\\s+as\\s+")[0];
            Pattern COL_EXPR = Pattern.compile("(\\w+?)\\s*(\\p{Punct}{1,2})\\s*('?\\w.*)");
            //Pattern COND_EXPR = Pattern.compile("(\\w+?)\\s*(\\p{Punct}{1,2})\\s*('?\\w.*)");
            Matcher m;
            if ((m = COL_EXPR.matcher(expr)).matches()) {
                Table t = this.getColumn(m.group(1).trim()); //
                Operator operator;
                switch (m.group(2).trim()) {
                    case "+":
                        operator = new Adder();
                        break;
                    case "-":
                        operator = new Subtractor();
                        break;
                    case "*":
                        operator = new Multiplier();
                        break;
                    case "/":
                        operator = new Divider();
                        break;
                    default:
                        throw new BadOperationException("No such operation");
                }
                if (Type.type(m.group(3).trim()).equals(Type.COLNAME)) {
                    Table t2 = this.getColumn(m.group(3).trim());
                    return combineCols(t, t2, colName, operator);
                } else {
                    return combineCols(t, new Item(m.group(3).trim(), Type.type(m.group(3).trim())), colName, operator);
                }

            } else {

                throw new BadOperationException("No such operation");
            }
        }
    }

    public Table getColumn(String s) {
        ColumnSpec returnColSpec = new ColumnSpec();
        for (Column c : columnSpec.columns) {
            if (c.colName.equals(s)) {
                returnColSpec.addColumn(c);
            }
        }
        Table returnVal = new Table(returnColSpec);
        for (Row r : this.tableRows) {
            returnVal.addRowColTable(r.getItem(s));
        }
        return returnVal;
    }
}