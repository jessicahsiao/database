/**
package db;

public class IntegerItem implements Item {
    
    Integer integer;

    public IntegerItem(int illest) {
        integer = illest;
    }

    @Override
    public Type colType() {
        return Type.INT;
    }

    @Override
    public Item add(Item i) {
        if (i.colType() == Type.INT) {
            int sum = this.integer + ((IntegerItem) i).integer;
            return new IntegerItem(sum);
        } else if (i.colType() == Type.FLOAT) {
            float sum = this.integer + ((FloatItem) i).f;
            return new FloatItem(sum);
        } else {
            //throw Error;
        }
    }

    @Override
    public Item greaterThan(Item i) {
        if (i.colType() == Type.INT) {
            int compare = this.integer.compareTo(((IntegerItem) i).integer);
            if (compare < 0) {
                return i;
            } else {
                return this;
            }
        } else if (i.colType() == Type.FLOAT) {
            Float z = (float) this.integer;
            int compare = z.compareTo(((FloatItem) i).f);
            if (compare < 0) {
                return i;
            } else {
                return this;
            }
        } else {
            //throw Error;
        }
    }

    @Override
    public Item lessThan(Item i) {
        if (i.colType() == Type.INT) {
            int compare = this.integer.compareTo(((IntegerItem) i).integer);
            if (compare < 0) {
                return this;
            } else {
                return i;
            }
        } else if (i.colType() == Type.FLOAT) {
            Float z = (float) this.integer;
            int compare = z.compareTo(((FloatItem) i).f);
            if (compare < 0) {
                return this;
            } else {
                return i;
            }
        } else {
            //throw Error;
        }
    }

    @Override
    public Item greaterThanEqualTo(Item i) {
        if (i.colType() == Type.INT) {
            int compare = this.integer.compareTo(((IntegerItem) i).integer);
            if (compare <= 0) {
                return i;
            } else {
                return this;
            }
        } else if (i.colType() == Type.FLOAT) {
            Float z = (float) this.integer;
            int compare = z.compareTo(((FloatItem) i).f);
            if (compare <= 0) {
                return i;
            } else {
                return this;
            }
        } else {
            //throw Error;
        }
    }

    @Override
    public Item lessThanEqualTo(Item i) {
        if (i.colType() == Type.INT) {
            int compare = this.integer.compareTo(((IntegerItem) i).integer);
            if (compare <= 0) {
                return this;
            } else {
                return i;
            }
        } else if (i.colType() == Type.FLOAT) {
            Float z = (float) this.integer;
            int compare = z.compareTo(((FloatItem) i).f);
            if (compare <= 0) {
                return this;
            } else {
                return i;
            }
        } else {
            //throw Error;
        }
    }

    @Override
    public boolean notEquals(Item i) {
        if (i.colType() == Type.INT) {
            int compare = this.integer.compareTo(((IntegerItem) i).integer);
            if (compare == 0) {
                return true;
            } else {
                return false;
            }
        } else if (i.colType() == Type.FLOAT) {
            Float z = (float) this.integer;
            int compare = z.compareTo(((FloatItem) i).f);
            if (compare == 0) {
                return true;
            } else {
                return false;
            }
        } else {
            //throw Error;
        }
    }

    @Override
    public String itemToString(Item i) {
        return integer.toString();
    }

    public float intToFloat(IntegerItem i) {
        float f = i.integer;
        return f;
    }


}
*/