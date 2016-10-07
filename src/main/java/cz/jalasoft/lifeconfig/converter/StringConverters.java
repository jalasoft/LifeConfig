package cz.jalasoft.lifeconfig.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * A factory of various converters from string.
 *
 * Created by honzales on 18.4.15.
 */
public final class StringConverters {

    static Converter toByte() {

        return new TypesafeConverter<String, Byte>(String.class, Byte.class) {
            @Override
            protected Byte convertSafely(String from) throws ConverterException {
                try {
                    return Byte.valueOf(from);
                } catch (NumberFormatException exc) {
                    throw new ConverterException(from, String.class, Byte.class);
                }
            }
        };
    }

    static Converter toFloat() {
        return new TypesafeConverter<String, Float>(String.class, Float.class) {
            @Override
            protected Float convertSafely(String from) throws ConverterException {
                try {
                    return Float.valueOf(from);
                } catch (NumberFormatException exc) {
                    throw new ConverterException(from, String.class, Float.class);
                }
            }
        };
    }

    static Converter toDouble() {
        return new TypesafeConverter<String, Double>(String.class, Double.class) {
            @Override
            protected Double convertSafely(String from) throws ConverterException {
                try {
                    return Double.valueOf(from);
                } catch (NumberFormatException exc) {
                    throw new ConverterException(from, String.class, Double.class);
                }
            }
        };
    }

    static Converter toShort() {
        return new TypesafeConverter<String, Short>(String.class, Short.class) {
            @Override
            protected Short convertSafely(String from) throws ConverterException {
                try {
                    return Short.valueOf(from);
                } catch (NumberFormatException exc) {
                    throw new ConverterException(from, String.class, Short.class);
                }
            }
        };
    }

    static Converter toInteger() {
        return new TypesafeConverter<String, Integer>(String.class, Integer.class) {
            @Override
            protected Integer convertSafely(String from) throws ConverterException {
                try {
                    return Integer.valueOf(from);
                } catch (NumberFormatException exc) {
                    throw new ConverterException(from, String.class, Integer.class);
                }
            }
        };
    }

    static Converter toLong() {
        return new TypesafeConverter<String, Long>(String.class, Long.class) {
            @Override
            protected Long convertSafely(String from) throws ConverterException {
                try {
                    return Long.parseLong(from);
                } catch (NumberFormatException exc) {
                    throw new ConverterException(from, String.class, Long.class);
                }
            }
        };
    }

    static Converter toBoolean() {
        return new TypesafeConverter<String, Boolean>(String.class, Boolean.class) {
            @Override
            protected Boolean convertSafely(String from) throws ConverterException {
                try {
                    return Boolean.parseBoolean(from);
                } catch (NumberFormatException exc) {
                    throw new ConverterException(from, String.class, Boolean.class);
                }
            }
        };
    }

    static Converter toFloatPrimitive() {
        return new TypesafeConverter<String, Float>(String.class, float.class) {
            @Override
            protected Float convertSafely(String from) throws ConverterException {
                try {
                    return Float.parseFloat(from);
                } catch (NumberFormatException exc) {
                    throw new ConverterException(from, String.class, float.class);
                }
            }
        };
    }

    static Converter toDoublePrimitive() {
        return new TypesafeConverter<String, Double>(String.class, double.class) {
            @Override
            protected Double convertSafely(String from) throws ConverterException {
                try {
                    return Double.parseDouble(from);
                } catch (NumberFormatException exc) {
                    throw new ConverterException(from, String.class, Double.class);
                }
            }
        };
    }

    static Converter toBooleanPrimitive() {
        return new TypesafeConverter<String, Boolean>(String.class, boolean.class) {
            @Override
            protected Boolean convertSafely(String from) throws ConverterException {
                try {
                    return Boolean.parseBoolean(from);
                } catch (NumberFormatException exc) {
                    throw new ConverterException(from, String.class, boolean.class);
                }
            }
        };
    }

    static Converter toBytePrimitive() {
        return new TypesafeConverter<String, Byte>(String.class, byte.class) {
            @Override
            protected Byte convertSafely(String from) throws ConverterException {
                try {
                    return Byte.parseByte(from);
                } catch (NumberFormatException exc) {
                    throw new ConverterException(from, String.class, byte.class);
                }
            }
        };
    }

    static Converter toShortPrimitive() {
        return new TypesafeConverter<String, Short>(String.class, short.class) {
            @Override
            protected Short convertSafely(String from) throws ConverterException {
                try {
                    return Short.parseShort(from);
                } catch (NumberFormatException exc) {
                    throw new ConverterException(from, String.class, Short.class);
                }
            }
        };
    }

    static Converter toIntPrimitive() {
        return new TypesafeConverter<String, Integer>(String.class, int.class) {
            @Override
            protected Integer convertSafely(String from) throws ConverterException {
                try {
                    return Integer.parseInt(from);
                } catch (NumberFormatException exc) {
                    throw new ConverterException(from, String.class, Integer.class);
                }
            }
        };
    }

    static Converter toLongPrimitive() {
        return new TypesafeConverter<String, Long>(String.class, long.class) {
            @Override
            protected Long convertSafely(String from) throws ConverterException {
                try {
                    return Long.parseLong(from);
                } catch (NumberFormatException exc) {
                    throw new ConverterException(from, String.class, Long.class);
                }
            }
        };
    }

    static Converter toISOLocalDateTime() {
        return new TypesafeConverter<String, LocalDateTime>(String.class, LocalDateTime.class) {
            @Override
            protected LocalDateTime convertSafely(String from) throws ConverterException {
                try {
                    return LocalDateTime.parse(from, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                } catch (DateTimeParseException exc) {
                    throw new ConverterException(from, String.class, LocalDateTime.class);
                }
            }
        };
    }

    //----------------------------------------------------------------
    //CONSTRUCTOR
    //----------------------------------------------------------------

    private StringConverters() {
        throw new RuntimeException();
    }
}
