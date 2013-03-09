package org.bitbucket.googolplex.devourer.paths.groups;

import com.google.common.base.Optional;
import org.bitbucket.googolplex.devourer.paths.patterns.PathPattern;

/**
 * This interface represent a group of paths which can be queried for a path which matches given
 * {@link PathPattern}. For example, the group can wrap an instance of {@code Set&lt;SimplePath&gt;} and just
 * return the same path if it is present in the set. Other strategies are possible too.
 */
public interface PathGroup {
    Optional<PathPattern> lookup(PathPattern path);
}
