package org.bitbucket.googolplex.devourer.integration.data;

import com.google.common.base.Preconditions;

/**
 * Date: 15.03.13
 * Time: 13:43
 *
 * @author Vladimir Matveev
 */
public class SourceFile {
    public final String name;
    public final String type;
    public final String content;

    public SourceFile(String name, String type, String content) {
        Preconditions.checkNotNull(name, "Name is null");
        Preconditions.checkNotNull(type, "Type is null");
        Preconditions.checkNotNull(content, "Content is null");

        this.name = name;
        this.type = type;
        this.content = content;
    }
}
