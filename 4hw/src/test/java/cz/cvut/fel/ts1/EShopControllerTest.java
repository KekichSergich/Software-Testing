package cz.cvut.fel.ts1;

import cz.cvut.fel.ts1.archive.PurchasesArchive;
import cz.cvut.fel.ts1.shop.EShopController;
import cz.cvut.fel.ts1.shop.ShoppingCart;
import cz.cvut.fel.ts1.shop.StandardItem;
import cz.cvut.fel.ts1.storage.NoItemInStorage;
import cz.cvut.fel.ts1.storage.Storage;
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
        StandardItem item = new StandardItem(1, "A", 50.0f, "Cat", 10);
        EShopController.startEShop();

        Storage storage = new Storage();
        storage.insertItems(item, 2);

        ShoppingCart cart = new ShoppingCart();
        cart.addItem(item);
        EShopController.purchaseShoppingCart(cart, "Tester", "Praha");
    }

    @Test
    void testPurchaseEmptyCartLogsError() {
        ShoppingCart cart = new ShoppingCart();
        assertDoesNotThrow(() -> {
            EShopController.purchaseShoppingCart(cart, "Empty", "Brno");
        });
    }
}
