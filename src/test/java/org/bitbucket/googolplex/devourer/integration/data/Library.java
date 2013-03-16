package org.bitbucket.googolplex.devourer.integration.data;

import com.google.common.base.Preconditions;

/**
 * Date: 15.03.13
 * Time: 13:43
 *
 * @author Vladimir Matveev
 */
public class Library {
    public final String groupId;
    public final String artifactId;
    public final String version;

    public Library(String groupId, String artifactId, String version) {
        Preconditions.checkNotNull(groupId, "Group id is null");
        Preconditions.checkNotNull(artifactId, "Artifact id is null");
        Preconditions.checkNotNull(version, "Version is null");

        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }
}
