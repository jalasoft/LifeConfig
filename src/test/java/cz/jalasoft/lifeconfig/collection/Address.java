package cz.jalasoft.lifeconfig.collection;


/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-12.
 */
public final class Address {

    private final String town;
    private final String country;
    private final String street;
    private final String buildingNumber;

    public Address(String town, String country, String street, String buildingNumber) {
        this.town = town;
        this.country = country;
        this.street = street;
        this.buildingNumber = buildingNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Address)) {
            return false;
        }

        Address that = (Address) obj;

        if (!this.country.equals(that.country)) {
            return false;
        }

        if (!this.town.equals(that.town)) {
            return false;
        }

        if (!this.street.equals(that.street)) {
            return false;
        }

        return this.buildingNumber.equals(that.buildingNumber);
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = result * 37 + country.hashCode();
        result = result * 37 + town.hashCode();
        result = result * 37 + street.hashCode();
        result = result * 37 + buildingNumber.hashCode();

        return result;
    }
}
