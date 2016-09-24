package cz.jalasoft.lifeconfig.basic;

/**
 * Created by honzales on 12.5.15.
 */
public class MyDay {

    public enum Month {
        JANUARY, FEBRUARY, MARCH;
    }

    private final int day;
    private final Month month;

    public MyDay(int day, Month month) {
        this.day = day;
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public Month getMonth() {
        return month;
    }
}
