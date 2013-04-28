/*******************************************************************************
 * Copyright 2013 Vladimir Matveev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.bitbucket.googolplex.devourer;

import com.google.common.base.Preconditions;
import org.bitbucket.googolplex.devourer.configuration.DevourerConfig;
import org.bitbucket.googolplex.devourer.configuration.annotated.MappingReflector;
import org.bitbucket.googolplex.devourer.configuration.modular.MappingModule;
import org.bitbucket.googolplex.devourer.configuration.modular.binders.MappingBinder;
import org.bitbucket.googolplex.devourer.configuration.modular.binders.impl.MappingBinderImpl;
import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.bitbucket.googolplex.devourer.exceptions.MappingException;
import org.bitbucket.googolplex.devourer.paths.mappings.PathMapping;

import javax.xml.stream.XMLInputFactory;
import java.util.Map;

/**
 * Contains factory methods for creating {@link Devourer} instances.
 */
public class Devourers {
    /**
     * Creates new {@link Devourer} with actions defined in the provided {@link MappingModule} using
     * default configuration.
     *
     * @param module mapping module with configured actions
     * @return new Devourer instance
     */
    public static Devourer create(MappingModule module) {
        return create(DevourerConfig.builder().build(), module);
    }

    /**
     * Creates new {@link Devourer} with actions defined in the provided {@link MappingModule} using
     * specified configuration.
     *
     * @param devourerConfig configuration object
     * @param module mapping module with configured actions
     * @return new Devourer instance
     */
    public static Devourer create(DevourerConfig devourerConfig, MappingModule module) {
        Preconditions.checkNotNull(devourerConfig, "Devourer config is null");
        Preconditions.checkNotNull(module, "Module object is null");

        MappingBinder binder = new MappingBinderImpl();
        try {
            module.configure(binder);
        } catch (RuntimeException e) {
            throw new MappingException("An exception occured during mapping module configuration", e);
        }

        PathMapping pathMapping = binder.getMapping();
        NamespaceContext namespaceContext = binder.getNamespaceContext();

        return new Devourer(devourerConfig, createXMLInputFactory(devourerConfig), pathMapping, namespaceContext);
    }

    /**
     * Creates new {@link Devourer} with actions defined in the annotated class of the provided object using
     * default configuration.
     *
     * @param configObject object of a class with annotated configuration
     * @return new Devourer instance
     */
    public static Devourer create(Object configObject) {
        return create(DevourerConfig.builder().build(), configObject);
    }

    /**
     * Creates new {@link Devourer} with actions defined in the annotated class of the provided object using
     * default configuration.
     *
     * @param devourerConfig configuration object
     * @param configObject object of a class with annotated configuration
     * @return new Devourer instance
     */
    public static Devourer create(DevourerConfig devourerConfig, Object configObject) {
        Preconditions.checkNotNull(devourerConfig, "Devourer config is null");
        Preconditions.checkNotNull(configObject, "Config object is null");

        MappingReflector reflector = new MappingReflector();
        reflector.collectMappings(configObject);

        PathMapping pathMapping = reflector.getMapping();
        NamespaceContext namespaceContext = reflector.getNamespaceContext();

        return new Devourer(devourerConfig, createXMLInputFactory(devourerConfig), pathMapping, namespaceContext);
    }

    private static XMLInputFactory createXMLInputFactory(DevourerConfig devourerConfig) {
        // Create input factory and configure it
        XMLInputFactory inputFactory = XMLInputFactory.newFactory();

        // We will set "reuse-instance" property to false; this is required for the default JDK6 StAX implementation
        // to be thread-safe
        try {
            inputFactory.setProperty("reuse-instance", Boolean.FALSE);
        } catch (IllegalArgumentException e) {
            // Ignore the exception
        }

        for (Map.Entry<String, Object> entry : devourerConfig.staxConfig.entrySet()) {
            inputFactory.setProperty(entry.getKey(), entry.getValue());
        }

        return inputFactory;
    }
}
