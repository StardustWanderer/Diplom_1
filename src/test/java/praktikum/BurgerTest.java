package praktikum;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BurgerTest {

    // Проверяем, что setBuns() сохраняет переданный объект булочки.
    @Test
    public void setBunsSetsCorrectBun() {
        // Arrange
        Burger burger = new Burger();
        Bun bun = new Bun("black bun", 100);

        // Act
        burger.setBuns(bun);

        // Assert: сохранена та же самая булочка.
        assertSame(bun, burger.bun);
    }

    // Проверяем добавление ингредиента в список.
    @Test
    public void addIngredientAddsIngredientToList() {
        // Arrange
        Burger burger = new Burger();

        Ingredient ingredient = new Ingredient(
                IngredientType.FILLING,
                "cutlet",
                100
        );

        // Act
        burger.addIngredient(ingredient);

        // Assert: в списке появился добавленный ингредиент
        assertEquals(List.of(ingredient), burger.ingredients);
    }

    // Проверяем удаление ингредиента по индексу.
    @Test
    public void removeIngredientRemovesIngredientByIndex() {
        // Arrange
        Burger burger = new Burger();

        Ingredient first = new Ingredient(
                IngredientType.SAUCE,
                "hot sauce",
                100
        );

        Ingredient second = new Ingredient(
                IngredientType.FILLING,
                "cutlet",
                200
        );

        burger.addIngredient(first);
        burger.addIngredient(second);

        // Act: удаляем первый ингредиент.
        burger.removeIngredient(0);

        // Assert: в списке остался только второй ингредиент.
        assertEquals(List.of(second), burger.ingredients);
    }

    // Проверяем изменение порядка ингредиентов.
    @Test
    public void moveIngredientChangesIngredientOrder() {
        // Arrange
        Burger burger = new Burger();

        Ingredient first = new Ingredient(
                IngredientType.FILLING,
                "cutlet",
                100
        );

        Ingredient second = new Ingredient(
                IngredientType.SAUCE,
                "hot sauce",
                200
        );

        Ingredient third = new Ingredient(
                IngredientType.FILLING,
                "sausage",
                300
        );

        burger.addIngredient(first);
        burger.addIngredient(second);
        burger.addIngredient(third);

        // Act: перемещаем элемент с индекса 0 на индекс 2.
        burger.moveIngredient(0, 2);

        // Assert: после перемещения мнгредиентов список имеет именно такой порядок: second, third, first.
        assertEquals(List.of(second, third, first), burger.ingredients);
    }

    // Проверяем расчёт стоимости бургера.
    @Test
    public void getPriceReturnsCorrectBurgerPrice() {
        // Arrange
        Burger burger = new Burger();

        Bun bun = mock(Bun.class);
        Ingredient first = mock(Ingredient.class);
        Ingredient second = mock(Ingredient.class);

        // Настраиваем цены тестовых объектов.
        when(bun.getPrice()).thenReturn(100f);
        when(first.getPrice()).thenReturn(50f);
        when(second.getPrice()).thenReturn(30f);

        burger.setBuns(bun);
        burger.addIngredient(first);
        burger.addIngredient(second);

        // Act
        float actualPrice = burger.getPrice();

        // Assert: 100 * 2 + 50 + 30 = 280.
        assertEquals(280f, actualPrice, 0.001f);
    }

    // Проверяем содержимое сформированного чека.
    @Test
    public void getReceiptReturnsCorrectReceipt() {
        // Arrange
        Burger burger = new Burger();

        Bun bun = mock(Bun.class);
        Ingredient sauce = mock(Ingredient.class);
        Ingredient filling = mock(Ingredient.class);

        // Настраиваем поведение мока булочки.
        when(bun.getName()).thenReturn("black bun");
        when(bun.getPrice()).thenReturn(100f);

        // Настраиваем поведение мока соуса.
        when(sauce.getType()).thenReturn(IngredientType.SAUCE);
        when(sauce.getName()).thenReturn("hot sauce");
        when(sauce.getPrice()).thenReturn(50f);

        // Настраиваем поведение мока начинки.
        when(filling.getType()).thenReturn(IngredientType.FILLING);
        when(filling.getName()).thenReturn("cutlet");
        when(filling.getPrice()).thenReturn(100f);

        burger.setBuns(bun);
        burger.addIngredient(sauce);
        burger.addIngredient(filling);

        // Act
        String actualReceipt = burger.getReceipt();

        // Assert: сравниваем чек целиком.
        String expectedReceipt = String.format(
                "(==== black bun ====)%n" +
                        "= sauce hot sauce =%n" +
                        "= filling cutlet =%n" +
                        "(==== black bun ====)%n" +
                        "%nPrice: %f%n",
                350f
        );

        assertEquals(expectedReceipt, actualReceipt);
    }
}