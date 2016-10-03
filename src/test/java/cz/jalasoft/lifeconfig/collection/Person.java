package cz.jalasoft.lifeconfig.collection;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-12.
 */
public class Person {

    private final String firstName;
    private final String lastName;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Person)) {
            return false;
        }

        Person that = (Person) obj;

        if (!this.firstName.equals(that.firstName)) {
            return false;
        }

        return this.lastName.equals(that.lastName);
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = result * 37 + firstName.hashCode();
        result = result * 37 + lastName.hashCode();

        return result;
    }
}
