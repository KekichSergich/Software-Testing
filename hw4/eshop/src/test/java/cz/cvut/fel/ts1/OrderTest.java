package cz.cvut.fel.ts1;

import cz.cvut.fel.ts1.shop.ShoppingCart;
import cz.cvut.fel.ts1.shop.StandardItem;
import cz.cvut.fel.ts1.shop.Order;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testConstructorWithState() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new StandardItem(1, "Test", 100.0f, "Test", 10));
        Order order = new Order(cart, "Alice", "Somewhere", 2);
        assertEquals("Alice", order.getCustomerName());
        assertEquals("Somewhere", order.getCustomerAddress());
        assertEquals(2, order.getState());
        assertEquals(1, order.getItems().size());
    }

    @Test
    void testConstructorWithoutState() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new StandardItem(2, "Another", 200.0f, "Cat", 5));
        Order order = new Order(cart, "Bob", "Elsewhere");
        assertEquals(0, order.getState());
    }

    @Test
    void testNullInputs() {
        ShoppingCart cart = new ShoppingCart();
        Order order = new Order(cart, null, null);
        assertNull(order.getCustomerName());
        assertNull(order.getCustomerAddress());
    }
}
