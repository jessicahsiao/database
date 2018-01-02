//package db;
//
//public class StringItem implements Item {
//
//    String string;
//
//    public StringItem(String str) {
//        string = str;
//    }
//
//    @Override
//    public Type type() {
//        return Type.STRING;
//    }
//
//    @Override
//    public Item add(Item i) {
//        if (i.type() == Type.STRING) {
//            String sum = this.string + ((StringItem) i).string;
//            return new StringItem(sum);
//        } else {
//            return i;
//        }
//    }
//
//    @Override
//    public Item greaterThan(Item i) {
//        if (i.type() == Type.STRING) {
//            int compare = this.string.compareTo(((StringItem) i).string);
//            if (compare < 0) {
//                return i;
//            } else {
//                return this;
//            }
//        } else {
//            return i;
//        }
//    }
//
//    @Override
//    public Item lessThan(Item i) {
//        if (i.type() == Type.STRING) {
//            int compare = this.string.compareTo(((StringItem) i).string);
//            if (compare < 0) {
//                return this;
//            } else {
//                return i;
//            }
//        } else {
//            return i;
//        }
//    }
//
//    @Override
//    public Item greaterThanEqualTo(Item i) {
//        if (i.type() == Type.STRING) {
//            int compare = this.string.compareTo(((StringItem) i).string);
//            if (compare <= 0) {
//                return i;
//            } else {
//                return this;
//            }
//        } else {
//            return i;
//        }
//    }
//
//    @Override
//    public Item lessThanEqualTo(Item i) {
//        if (i.type() == Type.STRING) {
//            int compare = this.string.compareTo(((StringItem) i).string);
//            if (compare <= 0) {
//                return this;
//            } else {
//                return i;
//            }
//        } else {
//            return i;
//        }
//    }
//
//    @Override
//    public boolean notEquals(Item i) {
//        if (i.type() == Type.STRING) {
//            int compare = this.string.compareTo(((StringItem) i).string);
//            if (compare == 0) {
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            return true;
//        }
//    }
//
//    @Override
//    public String itemToString(Item i) {
//        return string;
//    }
//
//
//    public Item[] split() {
//        String[] sArray = this.string.split(",");
//        int length = sArray.length;
//        StringItem[] sItemArray = new StringItem[length];
//
//        for (int i = 0; i < length; i++) {
//            StringItem sItem = new StringItem(sArray[i]);
//            sItemArray[i] = sItem;
//        }
//
//        return sItemArray;
//    }
//
//}
