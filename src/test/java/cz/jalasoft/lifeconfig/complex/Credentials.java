package cz.jalasoft.lifeconfig.complex;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-14.
 */
public final class Credentials {

    private final String username;
    private final String password;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Credentials)) {
            return false;
        }

        Credentials that = (Credentials) obj;

        if (!this.username.equals(that.username)) {
            return false;
        }

        return this.password.equals(that.password);
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = result * 37 + username.hashCode();
        result = result * 37 + password.hashCode();

        return result;
    }
}
