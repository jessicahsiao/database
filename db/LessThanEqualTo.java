package db;

/**
 * Created by Mia on 3/7/17.
 */
public class LessThanEqualTo implements Conditional {
    @Override
    public boolean evaluate(Item i1, Item i2) {

        switch (i1.type){
            case STRING:
                switch (i2.type){
                    case STRING:
                        String str1 = i1.value;
                        String str2 = i2.value;
                        if (str1.compareTo(str2) < 0) {
                            return true;
                        } else if (str1.compareTo(str2) == 0) {
                            return true;
                        } else {
                            return false;
                        }
                }

            case FLOAT:
                switch (i2.type){
                    case INT:
                        return (java.lang.Float.parseFloat(i1.value) <= java.lang.Integer.parseInt(i2.value));

                    case FLOAT:
                        return (java.lang.Float.parseFloat(i1.value) <= java.lang.Float.parseFloat(i2.value));

                }
            case INT:
                switch (i2.type){
                    case INT:
                        return (java.lang.Integer.parseInt(i1.value) <= java.lang.Integer.parseInt(i2.value));
                    case FLOAT:
                        return (java.lang.Integer.parseInt(i1.value) <= java.lang.Float.parseFloat(i2.value));

                }
            default:
                throw new BadOperationException("Bad comparison");
        }
    }
}
