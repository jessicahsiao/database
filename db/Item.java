package db;

/**
 * Created by jessicahsiao on 3/4/17.
 */
public class Item {

    String value;
    Type type;

    public Item(String v, Type type) {
        value = v;
        this.type = type;
    }
    public String toString(){
        if (Type.type(value) == Type.FLOAT) {
            float f = Float.parseFloat(value);
            String fString = String.format("%.3f", f);
            return fString;
        }
        return value;
    }

//
//    public Float getFloatItem() {
//        return Float.parseFloat(value);
//
//    }
//
//    public Integer getIntItem() {
//        return Integer.parseInt(value);
//    }
//
//    public String getStringItem() {
//        return value;
//    }

}
