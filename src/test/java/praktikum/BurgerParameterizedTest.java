package praktikum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/*
 * @RunWith(Parameterized.class) сообщает JUnit 4,
 * что класс содержит параметризованные тесты.
 *
 * JUnit создаст отдельный экземпляр тестового класса
 * для каждого набора входных данных.
 */
@RunWith(Parameterized.class)
public class BurgerParameterizedTest {

    // Цена одной булочки.
    private final float bunPrice;

    // Цены ингредиентов для текущего набора тестовых данных.
    private final List<Float> ingredientPrices;

    // Ожидаемая итоговая стоимость бургера.
    private final float expectedPrice;

    /*
     * JUnit передаёт значения из getParameters()
     * в конструктор для каждого набора данных.
     */
    public BurgerParameterizedTest(
            float bunPrice,
            List<Float> ingredientPrices,
            float expectedPrice
    ) {
        this.bunPrice = bunPrice;
        this.ingredientPrices = ingredientPrices;
        this.expectedPrice = expectedPrice;
    }

    /*
     * name задаёт понятное название каждого запуска теста.
     *
     * {index} — порядковый номер набора данных.
     * {0}     — цена булочки.
     * {1}     — список цен ингредиентов.
     * {2}     — ожидаемая стоимость бургера.
     */
    @Parameterized.Parameters(
            name = "Тест #{index}: цена булочки={0}, " +
                    "цены ингредиентов={1}, ожидаемая цена={2}"
    )
    public static Object[][] getParameters() {
        return new Object[][]{
                // Булочка 100, без ингредиентов: 100 * 2 = 200.
                {100f, Collections.<Float>emptyList(), 200f},

                // Булочка 100, ингредиент 50: 100 * 2 + 50 = 250.
                {100f, Arrays.asList(50f), 250f},

                // Булочка 100, ингредиенты 50 и 30: 100 * 2 + 50 + 30 = 280.
                {100f, Arrays.asList(50f, 30f), 280f},

                // Булочка 200, ингредиенты 100 и 50: 200 * 2 + 100 + 50 = 550.
                {200f, Arrays.asList(100f, 50f), 550f},

                // Булочка 300, ингредиенты 100, 200 и 300: итоговая цена 1200.
                {300f, Arrays.asList(100f, 200f, 300f), 1200f}
        };
    }

    @Test
    public void getPriceReturnsExpectedPrice() {
        // Arrange: создаём бургер.
        Burger burger = new Burger();

        // Создаём мок булочки и задаём её цену.
        Bun bun = mock(Bun.class);
        when(bun.getPrice()).thenReturn(bunPrice);

        burger.setBuns(bun);

        /*
         * Создаём отдельный мок для каждого ингредиента.
         * Его цена берётся из текущего набора параметров.
         */
        for (float ingredientPrice : ingredientPrices) {
            Ingredient ingredient = mock(Ingredient.class);

            when(ingredient.getPrice()).thenReturn(ingredientPrice);

            burger.addIngredient(ingredient);
        }

        // Act: рассчитываем итоговую стоимость.
        float actualPrice = burger.getPrice();

        // Assert: проверяем ожидаемую стоимость с допустимой погрешностью.
        assertEquals(expectedPrice, actualPrice, 0.001f);
    }
}