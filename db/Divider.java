package db;

/**
 * Created by jessicahsiao on 3/6/17.
 */
public class Divider implements Operator {
    @Override
    public Item evaluate(Item i1, Item i2) {
        switch (i1.type){
//            case STRING:
//                switch (i2.type){
//                    case STRING:
//                        return new Item(i1.value.substring(0,i1.value.length()-1) + i2.value.substring(1), Type.STRING);
//                }
            case FLOAT:
                switch (i2.type){
                    case INT:
                        return new Item(String.valueOf(Float.parseFloat(i1.value) / Integer.parseInt(i2.value)),Type.FLOAT);
                    case FLOAT:
                        return new Item(String.valueOf(Float.parseFloat(i1.value) / Float.parseFloat(i2.value)),Type.FLOAT);

                }
            case INT:
                switch (i2.type){
                    case INT:
                        return new Item(String.valueOf(Integer.parseInt(i1.value) / Integer.parseInt(i2.value)),Type.FLOAT);
                    case FLOAT:
                        return new Item(String.valueOf(Integer.parseInt(i1.value) / Float.parseFloat(i2.value)),Type.FLOAT);

                }
            default:
                throw new BadOperationException("Bad operation");
        }
    }
}
