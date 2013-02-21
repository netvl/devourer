package org.googolplex.devourer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Date: 19.02.13
 * Time: 22:57
 *
 * @author Vladimir Matveev
 */
public class DefaultStacksTest {
    @Test
    public void testPush() throws Exception {
        Stacks stacks = new DefaultStacks();

        stacks.push(123);
        stacks.push("123");

        assertEquals("123", stacks.pop());
        assertEquals(123, stacks.pop());
    }

    @Test
    public void testPushStack() throws Exception {
        Stacks stacks = new DefaultStacks();

        stacks.push("stack1", 123);
        stacks.push("stack1", "abcd");
        stacks.push("stack2", 945);
        stacks.push("stack2", 234.2);

        assertEquals(234.2, stacks.pop("stack2"));
        assertEquals("abcd", stacks.pop("stack1"));
        assertEquals(945, stacks.pop("stack2"));
        assertEquals(123, stacks.pop("stack1"));
    }

    @Test
    public void testPeek() throws Exception {
        Stacks stacks = new DefaultStacks();

        stacks.push(123);
        stacks.push(123.5);

        assertEquals(123.5, stacks.peek());
        assertEquals(123.5, stacks.peek());
        assertEquals(123.5, stacks.pop());
        assertEquals(123, stacks.peek());
        assertEquals(123, stacks.peek());
    }

    @Test
    public void testPeekStack() throws Exception {
        Stacks stacks = new DefaultStacks();

        stacks.push("stack1", 123);
        stacks.push("stack1", 345);
        stacks.push("stack2", 753);
        stacks.push("stack2", 356);

        assertEquals(345, stacks.peek("stack1"));
        assertEquals(345, stacks.peek("stack1"));
        assertEquals(356, stacks.peek("stack2"));
        assertEquals(356, stacks.peek("stack2"));
        assertEquals(345, stacks.pop("stack1"));
        assertEquals(356, stacks.pop("stack2"));
        assertEquals(123, stacks.peek("stack1"));
        assertEquals(753, stacks.pop("stack2"));
    }
}
