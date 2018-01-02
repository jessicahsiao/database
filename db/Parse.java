//package db;
//import java.util.*;
//import java.util.regex.Pattern;
//import java.util.regex.Matcher;
//
//import java.util.StringJoiner;
//
//class Parse {
//    // Various common constructs, simplifies parsing.
//    private static final String REST  = "\\s*(.*)\\s*",
//            COMMA = "\\s*,\\s*",
//            AND   = "\\s+and\\s+";
//
//    // Stage 1 syntax, contains the command name.
//    private static final Pattern CREATE_CMD = Pattern.compile("create table " + REST),
//            LOAD_CMD   = Pattern.compile("load " + REST),
//            STORE_CMD  = Pattern.compile("store " + REST),
//            DROP_CMD   = Pattern.compile("drop table " + REST),
//            INSERT_CMD = Pattern.compile("insert into " + REST),
//            PRINT_CMD  = Pattern.compile("print " + REST),
//            SELECT_CMD = Pattern.compile("select " + REST);
//
//    // Stage 2 syntax, contains the clauses of commands.
//    private static final Pattern CREATE_NEW  = Pattern.compile("(\\S+)\\s+\\((\\S+\\s+\\S+\\s*" +
//            "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
//            SELECT_CLS  = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+" +
//                    "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+" +
//                    "([\\w\\s+\\-*/'<>=!.]+?(?:\\s+and\\s+" +
//                    "[\\w\\s+\\-*/'<>=!.]+?)*))?"),
//            CREATE_SEL  = Pattern.compile("(\\S+)\\s+as select\\s+" +
//                    SELECT_CLS.pattern()),
//            INSERT_CLS  = Pattern.compile("(\\S+)\\s+values\\s+(.+?" +
//                    "\\s*(?:,\\s*.+?\\s*)*)");
//    /**
//     public static void main(String[] args) {
//     if (args.length != 1) {
//     System.err.println("Expected a single query argument");
//     //return;
//     }
//
//     eval("create table t1 (x int, y int)", );
//     }
//     */
//    static String eval(String query, Database db) {
//        Matcher m;
//        String output;
//        if ((m = CREATE_CMD.matcher(query)).matches()) {
//            output = createTable(m.group(1), db);
//        } else if ((m = LOAD_CMD.matcher(query)).matches()) {
//            output = loadTable(m.group(1), db);
//        } else if ((m = STORE_CMD.matcher(query)).matches()) {
//            output = storeTable(m.group(1), db);
//        } else if ((m = DROP_CMD.matcher(query)).matches()) {
//            output = dropTable(m.group(1), db);
//        } else if ((m = INSERT_CMD.matcher(query)).matches()) {
//            output = insertRow(m.group(1));
//        } else if ((m = PRINT_CMD.matcher(query)).matches()) {
//            output = printTable(m.group(1), db);
//        } else if ((m = SELECT_CMD.matcher(query)).matches()) {
//            output = select(m.group(1));
//        } else {
//            output = "Malformed query:" + query;
//            //System.err.printf("Malformed query: %s\n", query);
//        }
//        return output;
//    }
//
//    private static String createTable(String expr, Database db) {
//        Matcher m;
//        String output;
//
//        if ((m = CREATE_NEW.matcher(expr)).matches()) {
//            String name = m.group(1);
//            System.out.println(m.group(1));
//            String[] colClauses = m.group(2).split(COMMA);
//            String[] colNames = new String[colClauses.length];
//            String[] colTypes = new String[colClauses.length];
//
//            for (int i = 0; i < colClauses.length; i += 1) {
//                String[] splitClause = colClauses[i].split("\\s+");
//                colNames[i] = splitClause[0];
//                colTypes[i] = splitClause[1];
//            }
//            //System.out.println(Arrays.toString(colNames));
//            //System.out.println(Arrays.toString(colTypes));
//
//            output = createNewTable(name, colNames, colTypes, db);
//
//        } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
//            output = createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4));
//        } else {
//            return "ERROR: .*";
//            //System.err.printf("Malformed create: %s\n", expr);
//        }
//
//        return output;
//    }
//    //String s = "create table t1 (x int, y int)"
//    private static String createNewTable(String name, String[] colNames, String[] colTypes, Database db) {
//        String output = db.createTable(name, colNames, colTypes);
//        return output;
//        //System.out.printf("You are trying to create a table named %s with the columnSpec %s\n", name, colSentence);
//    }
//
//    private static String createSelectedTable(String name, String exprs, String tables, String conds) {
//        //EXPRS
//        //split exprs into <operand0> <arithmetic operator> <operand1> as <column alias> with helper method and put into
//        //  string array
//        //  depending on length of string array and whether operand 1 is a column name(string) or literal(number), call
//        // db.createNewTableUnary or db.createNewTableBinary
//
//        //CONDS
//        //split conds into array <column name> <comparison> <literal> or <column0 name> <comparison> <column1 name>
//        //depending on colType of item at index 2 call condCalcUnary or condCalcBinary
//        // the cond calc functions should replace the entries in the rows with entries that will allow join to be called
//
//        //CONDCALC HELPER
//        //
//        //System.out.printf("You are trying to create a table named %s by selecting these expressions:" +
//        //  " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", name, exprs, tables, conds);
//
//        String output = "";
//        return output;
//
//    }
//
//    private static String loadTable(String name, Database db) {
//        String output = db.load(name);
//        return output;
//        //System.out.printf("You are trying to load the table named %s\n", name);
//    }
//
//    private static String storeTable(String name, Database db) {
//        Table tbl = db.tables.get(name);
//        String output = db.store(name, tbl);
//        return output;
//        //System.out.printf("You are trying to store the table named %s\n", name);
//    }
//
//    private static String dropTable(String name, Database db) {
//        String output = "";
//        return output;
//        //System.out.printf("You are trying to drop the table named %s\n", name);
//    }
//
//    //CHANGE ME
//    private static String insertRow(String expr) {
//        Matcher m = INSERT_CLS.matcher(expr);
//        String output = "";
//        if (!m.matches()) {
//            System.err.printf("Malformed insert: %s\n", expr);
//            return ""; //CHANGE ME
//        }
//        return "";
//        //System.out.printf("You are trying to insert the row \"%s\" into the table %s\n", m.group(2), m.group(1));
//    }
//
//    private static String printTable(String name, Database db) {
//        String output = db.print(name);
//        return output;
//        //System.out.printf("You are trying to print the table named %s\n", name);
//    }
//
//    //CHANGE ME
//    private static String select(String expr) {
//        Matcher m = SELECT_CLS.matcher(expr);
//        if (!m.matches()) {
//            System.err.printf("Malformed select: %s\n", expr);
//            return "";
//        }
//        return "";
//        //select(m.group(1), m.group(2), m.group(3));
//    }
//
//    private static String select(String exprs, String tables, String conds) {
//        return "";
//        //System.out.printf("You are trying to select these expressions:" +
//        //  " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", exprs, tables, conds);
//    }
//}