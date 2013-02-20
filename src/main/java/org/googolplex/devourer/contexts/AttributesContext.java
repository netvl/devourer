package org.googolplex.devourer.contexts;

import com.google.common.base.Optional;

import java.util.Collection;

/**
 * An extension of {@link ElementContext} which adds attribute accessing methods.
 */
public interface AttributesContext extends ElementContext {
    /**
     * @return a collection of all available attribute names
     */
    Collection<PrefixedName> attributeNames();

    /**
     * Returns an attribute with given local name and no namespace.
     *
     * @param name local name of the attribute
     * @return an attribute with {@code name} local name and without a namespace, if present
     */
    Optional<String> attribute(String name);

    /**
     * Returns an attribute with given local name and namespace.
     *
     * @param name local name of the attribute
     * @param prefix namespace prefix of the attribute
     * @return an attribute with {@code name} local name and {@code prefix} namespace prefix, if present
     */
    Optional<String> attribute(String name, String prefix);

    /**
     * Returns an attribute with given prefixed name.
     *
     * @param name full name of the attribute
     * @return an attribute with {@code name} full name, if present
     */
    Optional<String> attribute(PrefixedName name);
}
