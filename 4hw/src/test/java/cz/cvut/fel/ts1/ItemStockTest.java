package cz.cvut.fel.ts1;

import cz.cvut.fel.ts1.shop.StandardItem;
import cz.cvut.fel.ts1.storage.ItemStock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemStockTest {

    @Test
    void testConstructorAndInitialCount() {
        StandardItem item = new StandardItem(1, "Test", 100.0f, "Cat", 5);
        ItemStock stock = new ItemStock(item);
        assertEquals(0, stock.getCount());
        assertEquals(item, stock.getItem());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10})
    void testIncreaseItemCount(int value) {
        ItemStock stock = new ItemStock(new StandardItem(1, "X", 10, "Cat", 1));
        stock.IncreaseItemCount(value);
        assertEquals(value, stock.getCount());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3})
    void testDecreaseItemCount(int value) {
        ItemStock stock = new ItemStock(new StandardItem(2, "Y", 20, "Tools", 2));
        stock.IncreaseItemCount(5);
        stock.decreaseItemCount(value);
        assertEquals(5 - value, stock.getCount());
    }
}
