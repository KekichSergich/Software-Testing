package cz.cvut.fel.ts1;

import cz.cvut.fel.ts1.archive.PurchasesArchive;
import cz.cvut.fel.ts1.shop.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PurchasesArchiveTest {

    PurchasesArchive archive;

    @BeforeEach
    void setUp() {
        archive = new PurchasesArchive();
    }

    @Test
    void testGetHowManyTimesHasBeenItemSold() {
        StandardItem item = new StandardItem(1, "Test", 100, "Test", 10);
        assertEquals(0, archive.getHowManyTimesHasBeenItemSold(item));

        ShoppingCart cart = new ShoppingCart();
        cart.addItem(item);
        Order order = new Order(cart, "Name", "Address");
        archive.putOrderToPurchasesArchive(order);

        assertEquals(1, archive.getHowManyTimesHasBeenItemSold(item));
    }

    @Test
    void testPutOrderToPurchasesArchiveCreatesEntry() {
        Item item = mock(Item.class);
        when(item.getID()).thenReturn(42);
        when(item.toString()).thenReturn("MockItem");

        ShoppingCart cart = new ShoppingCart();
        cart.addItem(item);

        Order order = new Order(cart, "Customer", "Address");

        archive.putOrderToPurchasesArchive(order);
        assertEquals(1, archive.getHowManyTimesHasBeenItemSold(item));
    }
}
