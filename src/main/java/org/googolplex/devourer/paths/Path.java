package org.googolplex.devourer.paths;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Simple wrapper around {@link List} of strings. Used to represent a path inside XML document
 * during the parsing process.
 */
public class Path {
    public final List<String> elements;

    public Path(List<String> elements) {
        Preconditions.checkNotNull(elements);

        this.elements = ImmutableList.copyOf(elements);
    }

    public static Path fromString(String value) {
        List<String> parts = new ArrayList<String>();
        for (int i = 0;;) {
            int idx = value.indexOf("/", i);
            if (idx == -1) {
                parts.add(value.substring(i));
                break;
            }
            parts.add(value.substring(i, idx));
            i = idx+1;
        }
        for (Iterator<String> it = parts.iterator(); it.hasNext();) {
            String part = it.next();
            if (part.trim().isEmpty()) {
                it.remove();
            }
        }
        return new Path(parts);
    }

    public Path resolve(String value) {
        Path newPart = fromString(value);
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        builder.addAll(this.elements).addAll(newPart.elements);
        return new Path(builder.build());
    }

    public Path moveUp() {
        return new Path(this.elements.subList(0, elements.size()-1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Path path = (Path) o;

        return elements.equals(path.elements);
    }

    @Override
    public int hashCode() {
        return elements.hashCode();
    }

    @Override
    public String toString() {
        return "/" + Joiner.on("/").join(elements);
    }
}

