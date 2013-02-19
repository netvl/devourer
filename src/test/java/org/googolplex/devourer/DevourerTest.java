package org.googolplex.devourer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.googolplex.devourer.configuration.DevourerConfig;
import org.googolplex.devourer.sandbox.DevoConfig1;
import org.googolplex.devourer.sandbox.ExampleDataModule;
import org.googolplex.devourer.sandbox.ExampleData;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Date: 19.02.13
 * Time: 14:24
 */
public class DevourerTest {
    private static final String EXAMPLE =
        "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
        "<data>\n" +
        "  <datum id=\"0\">\n" +
        "    <name>Name</name>\n" +
        "    <arg>0.3</arg>\n" +
        "    <arg>0.2</arg>\n" +
        "    <header name=\"Header-1\">header 1 value</header>\n" +
        "    <header name=\"Header-2\">\n" +
        "      Some bigger value\n" +
        "    </header>\n" +
        "  </datum>\n" +
        "</data>";

    @Test
    public void testModularConfig() throws Exception {
        Devourer devourer = DevourerMaker.create(DevourerConfig.builder().setStripSpaces(true).build(),
                                                 new ExampleDataModule())
                                         .make();
        Stacks stacks = devourer.parse(EXAMPLE);

        List<ExampleData> dataList = stacks.pop();

        assertEquals(1, dataList.size());

        ExampleData data = dataList.get(0);
        assertEquals(0, data.id);
        assertEquals("Name", data.name);
        assertEquals(ImmutableList.of(0.3, 0.2), data.args);
        assertEquals(ImmutableMap.of("Header-1", "header 1 value", "Header-2", "Some bigger value"), data.headers);
    }

    @Test
    public void testAnnotatedConfig() throws Exception {
        Devourer devourer = DevourerMaker.create(DevourerConfig.builder().setStripSpaces(true).build(),
                                                 new DevoConfig1())
                                         .make();
        Stacks stacks = devourer.parse(EXAMPLE);

        List<ExampleData> dataList = stacks.pop(Constants.Stacks.RESULT_STACK);

        assertEquals(1, dataList.size());

        ExampleData data = dataList.get(0);
        assertEquals(0, data.id);
        assertEquals("Name", data.name);
        assertEquals(ImmutableList.of(0.3, 0.2), data.args);
        assertEquals(ImmutableMap.of("Header-1", "header 1 value", "Header-2", "Some bigger value"), data.headers);
    }
}
