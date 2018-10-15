package sample;

/**
 * Created by Miha on 26/11/2017.
 */
public class Par {

    public Par(){}

    public Par(Character c, Integer i) {
        this.c = c;
        this.i = i;
    }

    private Character c;
    private Integer i;

    public Character getC() {
        return c;
    }

    public void setC(Character c) {
        this.c = c;
    }

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }

    @Override
    public String toString() {

        return "'"+c+"'->"+i;
    }
}
