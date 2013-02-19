package org.googolplex.devourer;

import org.googolplex.devourer.sandbox.DevoConfig3;
import org.googolplex.devourer.sandbox.ExampleData;
import org.junit.Test;

import java.util.List;

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
    public void testName() throws Exception {
        Devourer devourer = Devourer.create(new DevoConfig3());
        Stacks stacks = devourer.parse(EXAMPLE);

        List<ExampleData> dataList = stacks.pop();
    }
}
