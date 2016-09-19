package cz.jalasoft.lifeconfig.converter;

import java.util.*;
import java.util.function.Supplier;

/**
 * A converter that transforms a collection of objects
 * to another collection of another type of objects
 * based on provided converter of particular items.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-10.
 */
public final class CollectionConverter implements Converter<Collection, Collection> {

    private static final Map<Class<?>, Supplier<Collection>> collectionFactory;

    static {
        collectionFactory = new HashMap<>();
        collectionFactory.put(Collection.class, () -> new ArrayList());
        collectionFactory.put(List.class, () -> new ArrayList());
        collectionFactory.put(Set.class, () -> new HashSet<>());
        collectionFactory.put(Queue.class, () -> new LinkedList());
    }

    //----------------------------------------------------------------------
    //INSTANCE SCOPE
    //----------------------------------------------------------------------

    private final Converter itemConverter;
    private final Class<?> collectionType;

    public CollectionConverter(Converter itemConverter, Class<?> collectionType) {
        this.itemConverter = itemConverter;
        this.collectionType = collectionType;
    }

    @Override
    public Collection convert(Collection from) throws ConverterException {
        Collection result = newCollection();

        for(Object element : from) {
            Object convertedElement = itemConverter.convert(element);
            result.add(convertedElement);
        }

        return result;
    }

    private Collection newCollection() {
        Supplier<Collection> factory = collectionFactory.get(collectionType);
        return factory.get();
    }

    @Override
    public Class<Collection> sourceType() {
        return Collection.class;
    }

    @Override
    public Class<Collection> targetType() {
        return Collection.class;
    }
}
