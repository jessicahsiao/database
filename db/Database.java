package db;

import java.io.FileReader;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.Writer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static db.Table.Join;


public class Database {

    HashMap<String, Table> tablesMap;

    public Database() {
        tablesMap = new HashMap<>();
    }

    public void addTable(Table t, String name) {
        tablesMap.put(name, t);
    }

    public Table getTable(String name) {
        Table t = tablesMap.get(name);
        if (t == null) {
            throw new NoTableException("No table with name " + name + " found");
        }
        return tablesMap.get(name);
    }

    public String transact(String query) {
        try {
            return eval(query);
        } catch (NullPointerException e) {
            return "ERROR: .*";
        }
    }

    // Various common constructs, simplifies parsing.
    private static final String REST = "\\s*(.*)\\s*",
            COMMA = "\\s*,\\s*",
            AND = "\\s+and\\s+";


    // Stage 1 syntax, contains the command name.
    private static final Pattern CREATE_CMD = Pattern.compile("create table " + REST),
            LOAD_CMD = Pattern.compile("load " + REST),
            STORE_CMD = Pattern.compile("store " + REST),
            DROP_CMD = Pattern.compile("drop table " + REST),
            INSERT_CMD = Pattern.compile("insert into " + REST),
            PRINT_CMD = Pattern.compile("print " + REST),
            SELECT_CMD = Pattern.compile("select " + REST);

    // Stage 2 syntax, contains the clauses of commands.
    private static final Pattern CREATE_NEW = Pattern.compile("(\\S+)\\s+\\((\\S+\\s+\\S+\\s*"
            +
            "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
            SELECT_CLS = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+"
                    +
                    "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+"
                    +
                    "([\\w\\s+\\-*/'<>=!.]+?(?:\\s+and\\s+"
                    +
                    "[\\w\\s+\\-*/'<>=!.]+?)*))?"),
            CREATE_SEL = Pattern.compile("(\\S+)\\s+as select\\s+"
                    +
                    SELECT_CLS.pattern()),
            INSERT_CLS = Pattern.compile("(\\S+)\\s+values\\s+(.+?"
                    +
                    "\\s*(?:,\\s*.+?\\s*)*)");


    /**
     * public static void main(String[] args) {
     * if (args.length != 1) {
     * System.err.println("Expected a single query argument");
     * //return;
     * }
     * <p>
     * eval("create table t1 (x int, y int)", );
     * }
     */
    public String eval(String query) {
        try {
            Matcher m;
            String output;
            if ((m = CREATE_CMD.matcher(query)).matches()) {
                output = createTable(m.group(1));
            } else if ((m = LOAD_CMD.matcher(query)).matches()) {
                output = loadTable(m.group(1));
            } else if ((m = STORE_CMD.matcher(query)).matches()) {
                output = storeTable(m.group(1));
            } else if ((m = DROP_CMD.matcher(query)).matches()) {
                output = dropTable(m.group(1));
            } else if ((m = INSERT_CMD.matcher(query)).matches()) {
                output = insertRow(m.group(1));
            } else if ((m = PRINT_CMD.matcher(query)).matches()) {
                output = printTable(m.group(1));
            } else if ((m = SELECT_CMD.matcher(query)).matches()) {
                output = select(m.group(1));
            } else {
                output = "ERROR: .*";
                //System.err.printf("Malformed query: %s\n", query);
            }
            return output;
        } catch (NoTableException e) {
            return "ERROR: " + e.getMessage();
        } catch (StoreTableException e) {
            return "ERROR: " + e.getMessage();
        } catch (MalformedInputException e) {
            return "ERROR: " + e.getMessage();
        } catch (AddRowException e) {
            return "ERROR: " + e.getMessage();
        } catch (BadOperationException e) {
            return "ERROR: " + e.getMessage();
        } catch (CreateTableException e) {
            return "ERROR: " + e.getMessage();
        } catch (MalformedColExprException e) {
            return "ERROR: " + e.getMessage();
        } catch (MalformedCondExprException e) {
            return "ERROR: " + e.getMessage();
        }
    }

    private String createTable(String expr) {
        Matcher m;
        expr = expr.trim();
        if ((m = CREATE_NEW.matcher(expr)).matches()) {
            return createNewTable(m.group(1), m.group(2).trim().split(COMMA));

        } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
            return createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4));
        } else {
            return "ERROR: .*";
            //System.err.printf("Malformed create: %s\n", expr);
        }

    }

    private ColumnSpec parseStringArray(String[] columns) {
        String[] colNames = new String[columns.length];
        String[] colTypes = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            colNames[i] = columns[i].split("\\s+")[0];
            colTypes[i] = columns[i].split("\\s+")[1];
        }
        Type[] types = new Type[colTypes.length];
        for (int i = 0; i < colTypes.length; i++) {
            if (Helpers.isLowerCase(colTypes[i])) {
                if (colTypes[i] != null) {
                    types[i] = Type.valueOf(colTypes[i].toUpperCase());
                } else {
                    throw new MalformedInputException(colTypes[i] + " is not a valid type.");
                }

            } else {
                throw new MalformedInputException(colTypes[i] + " is not a valid type.");
            }
        }
        ColumnSpec c = new ColumnSpec(colNames, types);
        return c;
    }


    //String s = "create table t1 (x int, y int)"
    private String createNewTable(String name, String[] columns) {
        ColumnSpec c = parseStringArray(columns);
        Table t = new Table(c);
        this.addTable(t, name);
        return "";
    }


    //select x,y from a,b

    private String createSelectedTable(String name, String exprs, String tables, String conds) {
        Table returnTable = selectHelper(exprs, tables, conds);
        this.addTable(returnTable, name);
        //System.out.print(returnTable.toString());
        return "";
    }

    private String loadTable(String name) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(name + ".tbl"));
            String columnSpec = in.readLine();
            String[] colClauses = columnSpec.split(COMMA);
            Table tbl = new Table(parseStringArray(colClauses));
            String currentLine;
            while ((currentLine = in.readLine()) != null) {
                tbl.addRow(currentLine.split(COMMA));
            }
            in.close();
            if (tablesMap.containsKey(name)) {
                tablesMap.replace(name, tbl);
            } else {
                tablesMap.put(name, tbl);
                //System.out.println(printTable(name)); //printing for test
            }

        } catch (FileNotFoundException e) {
            return "ERROR: .*";
        } catch (IOException e) {
            return "ERROR: .*";
        } catch (ArrayIndexOutOfBoundsException e) {
            return "ERROR: .*";
        }
        return "";
        //System.out.printf("You are trying to load the table named %s\n", name);
    }


    private String storeTable(String name) {
        Table t = this.getTable(name);
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(name + ".tbl"), "utf-8"))) {
            writer.write(t.toString());
        } catch (UnsupportedEncodingException e) {
            throw new StoreTableException("Unsupported encoding");
        } catch (FileNotFoundException e) {
            throw new StoreTableException("File " + name + " .tbl not found");
        } catch (IOException e) {
            throw new StoreTableException("Some sort of input error");
        }
        return "";
        //System.out.printf("You are trying to store the table named %s\n", name);
    }

    private String dropTable(String name) {
        this.getTable(name);
        this.tablesMap.remove(name);
        return "";
        //System.out.printf("You are trying to drop the table named %s\n", name);
    }

    private String insertRow(String expr) {
        Matcher m = INSERT_CLS.matcher(expr);
        String output = "";
        if (!m.matches()) {
            System.err.printf("Malformed insert: %s\n", expr);
            return ""; //CHANGE ME
        } else {
            Table t = this.getTable(m.group(1).trim());
            t.addRow(m.group(2).split(COMMA));
        }
        return "";
    }

    private String printTable(String name) {
        Table t = this.getTable(name);
        return t.toString();
        //System.out.printf("You are trying to print the table named %s\n", name);
    }

    private String select(String expr) {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            //System.err.printf("Malformed select: %s\n", expr);
            return "";
        }
        return select(m.group(1), m.group(2), m.group(3));
    }

    //select x+y as a, x*y as b from t1

    private Table evalCondExprs(String conds, Table allCols) {
        String[] condExprArray = conds.split(AND);
        Table tbl = allCols;
        //Table newTbl = allCols;

        // for every expression, reevaluate tbl
        for (String expr : condExprArray) {
            tbl = condExprHelper(tbl, expr);
        }
        return tbl;
    }

    // restructure to take in allCols
    // each expression specifies which cols it involves
    private Table condExprHelper(Table tbl, String expr) {
        Pattern condExpr = Pattern.compile("(\\w+?)\\s*(\\p{Punct}{1,2})\\s*('?\\w.*)");
        //if false, remove row
        Table returnTable = new Table(tbl.columnSpec);
        String col1Name; // name of first column
        String col2Name; //name of second column
        Matcher m;


        if ((m = condExpr.matcher(expr)).matches()) {
            col1Name = (m.group(1).trim()); // group 1 is the name of the first column
            Table col1 = tbl.getColumn(col1Name);

            Conditional conditional;
            switch (m.group(2).trim()) { // group 2 is the conditional
                case "==":
                    conditional = new Equals();
                    break;
                case "!=":
                    conditional = new NotEquals();
                    break;
                case "<":
                    conditional = new LessThan();
                    break;
                case ">":
                    conditional = new GreaterThan();
                    break;
                case "<=":
                    conditional = new LessThanEqualTo();
                    break;
                case ">=":
                    conditional = new GreaterThanEqualTo();
                    break;
                default:
                    throw new BadOperationException("No such conditonal");
            }

            //group 3 can either be a column name or a literal
            if (Type.type(m.group(3).trim()).equals(Type.COLNAME)) { // if group 3 is a column name
                col2Name = (m.group(3).trim());
                Table col2 = tbl.getColumn(col2Name);

                for (int i = 0; i < col1.tableRows.size(); i++) {
                    Item i1 = col1.tableRows.get(i).items[0];
                    Item i2 = col2.tableRows.get(i).items[0];
                    if (conditional.evaluate(i1, i2)) {
                        //tbl.tableRows.remove(i);
                        returnTable.addRow(tbl.tableRows.get(i));
                    }
                }

            } else {
                Item literal = new Item(m.group(3).trim(), Type.type(m.group(3).trim()));

                for (int i = 0; i < col1.tableRows.size(); i++) {
                    if (conditional.evaluate(col1.tableRows.get(i).items[0], literal)) {
                        //tbl.tableRows.remove(i);
                        returnTable.addRow(tbl.tableRows.get(i));
                    }
                }
            }

        } else {
            throw new MalformedCondExprException("Malformed conditional expression");

        }
        return returnTable;
    }

    private Table selectHelper(String exprs, String tables, String conds) {
        String[] tableNames = tables.split(COMMA);
        Table[] tableArray = new Table[tableNames.length];
        ArrayList<Item[]> rowItemArrayList = new ArrayList<>();
        String[] exprArray = exprs.split(COMMA);
        String[] colNamesArray = new String[exprArray.length];
        Type[] colTypesArray = new Type[exprArray.length];
        Table returnTable;
        Table[] newTableArray = new Table[exprArray.length];

        for (int i = 0; i < tableNames.length; i++) {
            tableArray[i] = tablesMap.get(tableNames[i]);
        }

        for (int i = 0; i < tableNames.length; i++) {
            tableArray[i] = getTable(tableNames[i]);
        }

        // join all tables to collect all columns
        Table allCols = Join(tableArray);

        if (conds != null) {
            allCols = evalCondExprs(conds, allCols);
        }
        if (!exprs.equals("*")) {

            // evaluate column expressions and generate and store new columns
            for (int i = 0; i < exprArray.length; i++) {
                Table newCol = allCols.evalColExpr(exprArray[i]);
                colNamesArray[i] = newCol.columnSpec.columns.get(0).colName;
                colTypesArray[i] = newCol.columnSpec.columns.get(0).colType;
                newTableArray[i] = newCol;

            }

            // populate rowItemArrayList
            for (int i = 0; i < newTableArray[0].tableRows.size(); i++) {
                Item[] row = new Item[newTableArray.length];
                for (int j = 0; j < newTableArray.length; j++) {
                    row[j] = newTableArray[j].tableRows.get(i).items[0];
                }
                rowItemArrayList.add(row);
            }

            // generate final table
            ColumnSpec colSpec = new ColumnSpec(colNamesArray, colTypesArray);
            allCols = new Table(colSpec);
            for (int i = 0; i < newTableArray[0].tableRows.size(); i++) {
                Row newRow = new Row(rowItemArrayList.get(i), colSpec);
                allCols.addRow(newRow);
            }

            returnTable = allCols;
        } else {
            returnTable = Join(tableArray); // if * given, join tables
        }

        return returnTable;
    }

    private String select(String exprs, String tables, String conds) {
        Table returnTable = selectHelper(exprs, tables, conds);
        String name = "beepboop";
        this.tablesMap.put(name, returnTable);
        return returnTable.toString();

    }
}
