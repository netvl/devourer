package org.googolplex.devourer.contexts;

import com.google.common.base.Optional;

import java.util.Collection;

/**
 * Date: 18.02.13
 * Time: 15:08
 */
public interface AttributesContext extends ElementContext {
    Collection<PrefixedName> attributeNames();
    Optional<String> attribute(String name);
    Optional<String> attribute(String name, String namespace);
}
