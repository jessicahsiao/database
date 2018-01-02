package db;

/**
 * Created by Mia on 3/7/17.
 */
public class NotEquals implements Conditional {
    @Override
    public boolean evaluate(Item i1, Item i2) {

        switch (i1.type){

            case FLOAT:
                switch (i2.type){
                    case INT:
                        return (java.lang.Float.parseFloat(i1.value) != java.lang.Integer.parseInt(i2.value));

                    case FLOAT:
                        return (java.lang.Float.parseFloat(i1.value) != java.lang.Float.parseFloat(i2.value));

                }
            case INT:
                switch (i2.type){
                    case INT:
                        return (java.lang.Integer.parseInt(i1.value) != java.lang.Integer.parseInt(i2.value));
                    case FLOAT:
                        return (java.lang.Integer.parseInt(i1.value) != java.lang.Float.parseFloat(i2.value));

                }

            case STRING:
                switch (i2.type) {
                    case STRING:
                        return (!(i1.value.equals(i2.value)));
                }

            default:
                throw new BadOperationException("Bad comparison");
        }
    }
}
