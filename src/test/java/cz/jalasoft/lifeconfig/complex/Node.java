package cz.jalasoft.lifeconfig.complex;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-14.
 */
public final class Node {

    private final String url;

    public Node(String url) {
        this.url = url;
    }

    public String url() {
        return url;
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = result * 37 + url.hashCode();

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof  Node)) {
            return false;
        }

        Node that = (Node) obj;

        return this.url.equals(that.url);
    }
}
