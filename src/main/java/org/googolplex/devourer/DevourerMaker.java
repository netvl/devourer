package org.googolplex.devourer;

import org.googolplex.devourer.configuration.DevourerConfig;
import org.googolplex.devourer.configuration.modular.MappingModule;
import org.googolplex.devourer.configuration.modular.binders.MappingBinder;
import org.googolplex.devourer.configuration.modular.binders.MappingBinderImpl;
import org.googolplex.devourer.paths.Path;
import org.googolplex.devourer.paths.PathMapping;

import javax.xml.stream.XMLInputFactory;
import java.util.Map;

/**
 * Date: 19.02.13
 * Time: 15:53
 */
public class DevourerMaker {
    private final Map<Path, PathMapping> mappings;
    private final DevourerConfig devourerConfig;

    private DevourerMaker(Map<Path, PathMapping> mappings, DevourerConfig devourerConfig) {
        this.mappings = mappings;
        this.devourerConfig = devourerConfig;
    }

    public static DevourerMaker create(MappingModule module) {
        return create(DevourerConfig.builder().build(), module);
    }

    public static DevourerMaker create(DevourerConfig devourerConfig, MappingModule module) {
        MappingBinder binder = new MappingBinderImpl();
        module.configure(binder);
        Map<Path, PathMapping> mappings = binder.mappings();

        return new DevourerMaker(mappings, devourerConfig);
    }

    public Devourer make() {
        // Create input factory and configure it
        XMLInputFactory inputFactory = XMLInputFactory.newFactory();
        for (Map.Entry<String, String> entry : devourerConfig.staxConfig.entrySet()) {
            inputFactory.setProperty(entry.getKey(), entry.getValue());
        }

        return new Devourer(devourerConfig, inputFactory, mappings);
    }
}
