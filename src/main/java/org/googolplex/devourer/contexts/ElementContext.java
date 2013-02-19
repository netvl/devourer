package org.googolplex.devourer.contexts;

import com.google.common.base.Optional;

/**
 * Date: 18.02.13
 * Time: 15:03
 */
public interface ElementContext {
    String elementName();
    Optional<String> elementNamespace();
}
