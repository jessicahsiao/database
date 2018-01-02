package db;

/**
 * Created by jessicahsiao on 3/1/17.
 */
public enum Type {
    FLOAT, STRING, INT, COLNAME;
    public static Type type(String s) {
        if (s.matches(("^-?\\d+$"))) {
            return Type.INT;
        } else if (s.matches("[-+]?[0-9]*\\.[0-9]*")) {
            return Type.FLOAT;
        } else if (s.matches("^'.*'$")) {
            return Type.STRING;
        }
        return Type.COLNAME;

    }
    public static Type mergeTypes(Type t1, Type t2){
        switch (t1) {
            case STRING:
                switch (t2){
                    case STRING:
                        return STRING;
                }
            case FLOAT:
                switch (t2){
                    case INT:
                        return FLOAT;
                    case FLOAT:
                        return FLOAT;

                }
            case INT:
                switch (t2){
                    case INT:
                        return INT;
                    case FLOAT:
                        return FLOAT;

                }
            default:
                throw new BadOperationException("Bad operation");
        }

    }
}
