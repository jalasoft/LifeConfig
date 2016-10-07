package cz.jalasoft.lifeconfig.basic;


import cz.jalasoft.lifeconfig.converter.ConverterException;
import cz.jalasoft.lifeconfig.converter.TypesafeConverter;

/**
 * Created by honzales on 12.5.15.
 */
public class MyDayConverter extends TypesafeConverter<String, MyDay> {

    public MyDayConverter() {
        super(String.class, MyDay.class);
    }

    @Override
    public MyDay convertSafely(String from) throws ConverterException {
        String[] words = from.split("\\s");
        if (words.length != 2) {
            throw new ConverterException(from, String.class, MyDay.class, "Unexpected number of words: " + words.length);
        }

        int day = convertDay(words);
        MyDay.Month month = convertMonth(words);

        return new MyDay(day, month);
    }

    private int convertDay(String[] words) throws ConverterException {
        try {
            return Integer.parseInt(words[0]);
        } catch (NumberFormatException exc) {
            throw new ConverterException(words[0], String.class, Integer.class, exc);
        }
    }

    private MyDay.Month convertMonth(String[] words) throws ConverterException {
        try {
            return MyDay.Month.valueOf(words[1]);
        } catch (IllegalArgumentException exc) {
            throw new ConverterException(words[1], String.class, MyDay.Month.class, "Unexpected format of month.");
        }
    }
}
