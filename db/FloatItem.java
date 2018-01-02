/**
package db;

public class FloatItem implements Item {

    Float f;

    public FloatItem(float flow) {
        f = flow;
    }

    @Override
    public Type colType() {
        return Type.FLOAT;
    }

    @Override
    public Item add(Item i) {
        if (i.colType() == Type.FLOAT) {
            float sum = this.f + ((FloatItem) i).f;
            return new FloatItem(sum);
        } else if (i.colType() == Type.INT) {
            float sum = this.f + ((IntegerItem) i).integer;
            return new FloatItem(sum);
        } else {
            //throw Error;
        }
    }



    @Override
    public Item greaterThan(Item i) {
        if (i.colType() == Type.FLOAT) {
            int compare = this.f.compareTo(((FloatItem) i).f);
            if (compare < 0) {
                return i;
            } else {
                return this;
            }
        } else if (i.colType() == Type.INT) {
            IntegerItem it = ((IntegerItem) i);
            float z = it.intToFloat(it);
            int compare = this.f.compareTo(z);
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
        if (i.colType() == Type.FLOAT) {
            int compare = this.f.compareTo(((FloatItem) i).f);
            if (compare < 0) {
                return this;
            } else {
                return i;
            }
        } else if (i.colType() == Type.INT) {
            IntegerItem it = ((IntegerItem) i);
            float z = it.intToFloat(it);
            int compare = this.f.compareTo(z);
            if (compare < 0) {
                return this;
            } else {
                return i;
            }
        } else {
            //throw Error;
        }
        }

    }

    @Override
    public Item greaterThanEqualTo(Item i) {
        if (i.colType() == Type.FLOAT) {
            int compare = this.f.compareTo(((FloatItem) i).f);
            if (compare <= 0) {
                return i;
            } else {
                return this;
            }
        } else if (i.colType() == Type.INT) {
            IntegerItem it = ((IntegerItem) i);
            float z = it.intToFloat(it);
            int compare = this.f.compareTo(z);
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
        if (i.colType() == Type.FLOAT) {
            int compare = this.f.compareTo(((FloatItem) i).f);
            if (compare <= 0) {
                return this;
            } else {
                return i;
            }
        } else if (i.colType() == Type.INT) {
            IntegerItem it = ((IntegerItem) i);
            float z = it.intToFloat(it);
            int compare = this.f.compareTo(z);
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
        if (i.colType() == Type.FLOAT) {
            int compare = this.f.compareTo(((FloatItem) i).f);
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
        return f.toString();
    }
}
 */