package cz.cvut.fel.ts1;

import cz.cvut.fel.ts1.shop.*;
import cz.cvut.fel.ts1.storage.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EShopControllerTest {

    @BeforeEach
    void setup() {
        EShopController.startEShop();
    }

    @Test
    void testNewCartAddsCart() {
        ShoppingCart cart = EShopController.newCart();
        assertNotNull(cart);
    }

    @Test
    void testPurchaseShoppingCartHappyPath() throws NoItemInStorage {
        // 1. Inicializuj item
        StandardItem item = new StandardItem(1, "A", 50.0f, "Cat", 10);

        // 2. Přidej item do EShop storage
        EShopController.getStorage().insertItems(item, 2);

        // 3. Přidej do košíku a nakup
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(item);

        assertDoesNotThrow(() -> {
            EShopController.purchaseShoppingCart(cart, "Tester", "Praha");
        });
    }

    @Test
    void testPurchaseEmptyCartLogsError() {
        ShoppingCart cart = new ShoppingCart();
        assertDoesNotThrow(() -> {
            EShopController.purchaseShoppingCart(cart, "Empty", "Brno");
        });
    }
}
