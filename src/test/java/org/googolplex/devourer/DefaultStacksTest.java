package org.googolplex.devourer;

import org.googolplex.devourer.stacks.DefaultStacks;
import org.googolplex.devourer.stacks.Stacks;
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

        stacks.get("stack1").push(123);
        stacks.get("stack1").push("abcd");
        stacks.get("stack2").push(945);
        stacks.get("stack2").push(234.2);

        assertEquals(234.2, stacks.get("stack2").pop());
        assertEquals("abcd", stacks.get("stack1").pop());
        assertEquals(945, stacks.get("stack2").pop());
        assertEquals(123, stacks.get("stack1").pop());
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

        stacks.get("stack1").push(123);
        stacks.get("stack1").push(345);
        stacks.get("stack2").push(753);
        stacks.get("stack2").push(356);

        assertEquals(345, stacks.get("stack1").peek());
        assertEquals(345, stacks.get("stack1").peek());
        assertEquals(356, stacks.get("stack2").peek());
        assertEquals(356, stacks.get("stack2").peek());
        assertEquals(345, stacks.get("stack1").pop());
        assertEquals(356, stacks.get("stack2").pop());
        assertEquals(123, stacks.get("stack1").peek());
        assertEquals(753, stacks.get("stack2").pop());
    }
}
