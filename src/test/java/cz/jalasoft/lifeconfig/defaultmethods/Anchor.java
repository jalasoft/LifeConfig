package cz.jalasoft.lifeconfig.defaultmethods;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-10-05.
 */
public interface Anchor {

    int line();

    int column();

    default Coordinate asCoordinate() {
        return new Coordinate(line(), column());
    }

    final class Coordinate {

        private final int row;
        private final int column;

        public Coordinate(int row, int column) {
            this.row = row;
            this.column = column;
        }

        public int row() {
            return row;
        }

        public int column() {
            return column;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Coordinate that = (Coordinate) o;

            if (row != that.row) return false;
            return column == that.column;

        }

        @Override
        public int hashCode() {
            int result = row;
            result = 31 * result + column;
            return result;
        }
    }
}
