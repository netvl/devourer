package org.bitbucket.googolplex.devourer.stacks;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

        assertFalse(stacks.tryPop().isPresent());

        stacks.push(123);
        stacks.push("123");

        assertEquals(Optional.of("123"), stacks.<String>tryPop());
        assertEquals(123, stacks.pop());
    }

    @Test
    public void testPushStack() throws Exception {
        Stacks stacks = new DefaultStacks();

        assertFalse(stacks.get("stack1").tryPop().isPresent());
        assertFalse(stacks.get("stack2").tryPop().isPresent());

        stacks.get("stack1").push(123);
        stacks.get("stack1").push("abcd");
        stacks.get("stack2").push(945);
        stacks.get("stack2").push(234.2);

        assertEquals(Optional.of(234.2), stacks.get("stack2").<Double>tryPop());
        assertEquals(Optional.of("abcd"), stacks.get("stack1").<String>tryPop());
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

    @Test
    public void testTryPeek() throws Exception {
        Stacks stacks = new DefaultStacks();

        assertFalse(stacks.tryPeek().isPresent());
        stacks.push(1234);
        assertEquals(Optional.of(1234), stacks.<Integer>tryPeek());
        assertEquals(Optional.of(1234), stacks.<Integer>tryPeek());
    }

    @Test
    public void testTryPeekStack() throws Exception {
        Stacks stacks = new DefaultStacks();

        assertFalse(stacks.get("stack1").tryPeek().isPresent());
        assertFalse(stacks.get("stack2").tryPeek().isPresent());

        stacks.get("stack1").push(123);
        assertEquals(Optional.of(123), stacks.get("stack1").<Integer>tryPeek());
        assertFalse(stacks.get("stack2").tryPeek().isPresent());
        stacks.get("stack2").push(456);
        assertEquals(Optional.of(123), stacks.get("stack1").<Integer>tryPeek());
        assertEquals(Optional.of(456), stacks.get("stack2").<Integer>tryPeek());
        assertEquals(Optional.of(456), stacks.get("stack2").<Integer>tryPeek());
    }

    @Test
    public void testIsEmpty() throws Exception {
        Stacks stacks = new DefaultStacks();

        assertTrue(stacks.isEmpty());
        stacks.push("abcd");
        assertFalse(stacks.isEmpty());
        stacks.pop();
        assertTrue(stacks.isEmpty());
    }

    @Test
    public void testIsEmptyStack() throws Exception {
        Stacks stacks = new DefaultStacks();

        assertTrue(stacks.get("stack1").isEmpty());
        assertTrue(stacks.get("stack2").isEmpty());

        stacks.get("stack1").push(123);
        assertFalse(stacks.get("stack1").isEmpty());
        assertTrue(stacks.get("stack2").isEmpty());
        stacks.get("stack2").push(456);
        assertFalse(stacks.get("stack1").isEmpty());
        assertFalse(stacks.get("stack2").isEmpty());
        stacks.get("stack1").pop();
        stacks.get("stack2").pop();
        assertTrue(stacks.get("stack1").isEmpty());
        assertTrue(stacks.get("stack2").isEmpty());
    }

    @Test
    public void testSize() throws Exception {
        Stacks stacks = new DefaultStacks();

        assertEquals(0, stacks.size());
        stacks.push(123);
        assertEquals(1, stacks.size());
        stacks.push(234);
        assertEquals(2, stacks.size());
        stacks.pop();
        stacks.pop();
        assertEquals(0, stacks.size());
    }

    @Test
    public void testSizeStack() throws Exception {
        Stacks stacks = new DefaultStacks();

        assertEquals(0, stacks.get("stack1").size());
        assertEquals(0, stacks.get("stack2").size());

        stacks.get("stack1").push(123);
        assertEquals(1, stacks.get("stack1").size());
        assertEquals(0, stacks.get("stack2").size());
        stacks.get("stack2").push(456);
        assertEquals(1, stacks.get("stack1").size());
        assertEquals(1, stacks.get("stack2").size());
        stacks.get("stack1").push(234);
        stacks.get("stack2").push(567);
        assertEquals(2, stacks.get("stack1").size());
        assertEquals(2, stacks.get("stack2").size());
        stacks.get("stack1").pop();
        stacks.get("stack1").pop();
        stacks.get("stack2").pop();
        stacks.get("stack2").pop();
        assertEquals(0, stacks.get("stack1").size());
        assertEquals(0, stacks.get("stack2").size());
    }

    @Test
    public void testPopList() throws Exception {
        Stacks stacks = new DefaultStacks();

        List<Object> list1 = stacks.popList();
        assertTrue(list1.isEmpty());

        stacks.push(1).push(2).push(3);
        List<Integer> list2 = stacks.popList();
        assertEquals(ImmutableList.of(1, 2, 3), list2);
        assertTrue(stacks.isEmpty());
    }

    @Test
    public void testPeekList() throws Exception {
        Stacks stacks = new DefaultStacks();

        List<Object> list1 = stacks.peekList();
        assertTrue(list1.isEmpty());

        stacks.push("a").push("b").push("c");
        List<String> list2 = stacks.peekList();
        assertEquals(ImmutableList.of("a", "b", "c"), list2);
        assertFalse(stacks.isEmpty());
        assertEquals(3, stacks.size());
        assertEquals("c", stacks.peek());
    }
}
