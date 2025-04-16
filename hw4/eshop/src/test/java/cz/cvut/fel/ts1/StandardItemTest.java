package cz.cvut.fel.ts1;

import cz.cvut.fel.ts1.shop.StandardItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class StandardItemTest {

    @Test
    void testConstructorAndGetters() {
        StandardItem item = new StandardItem(1, "Test", 100.0f, "TestCat", 10);
        assertEquals(1, item.getID());
        assertEquals("Test", item.getName());
        assertEquals(100.0f, item.getPrice());
        assertEquals("TestCat", item.getCategory());
        assertEquals(10, item.getLoyaltyPoints());
    }

    @Test
    void testCopy() {
        StandardItem original = new StandardItem(1, "Test", 100.0f, "TestCat", 10);
        StandardItem copy = original.copy();
        assertEquals(original, copy);
        assertNotSame(original, copy);
    }

    @ParameterizedTest
    @CsvSource({
            "1,Test,100.0,TestCat,10,true",
            "1,Test,100.0,TestCat,5,false",
            "2,Test,100.0,TestCat,10,false",
            "1,Test2,100.0,TestCat,10,false"
    })
    void testEquals(int id, String name, float price, String category, int points, boolean expected) {
        StandardItem original = new StandardItem(1, "Test", 100.0f, "TestCat", 10);
        StandardItem other = new StandardItem(id, name, price, category, points);
        assertEquals(expected, original.equals(other));
    }
}
