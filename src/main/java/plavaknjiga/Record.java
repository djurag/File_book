package plavaknjiga;

import java.util.Objects;

public class Record {
    private String name;
    private int act;

    public Record(String name, int act) {
        this.name = name;
        this.act = act;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getAct() {
        return act;
    }

    public void setAct(int act) {
        this.act = act;
    }

    @Override
    public String toString() {
        return name + ", Broj spisa: " + act;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return Objects.equals(name, record.name) &&
                Objects.equals(act, record.act);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, act);
    }
}
