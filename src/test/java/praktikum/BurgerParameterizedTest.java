package praktikum;

// JUnit 4: аннотация тестового метода.
import org.junit.Test;

// Класс запускает тест несколько раз с разными параметрами.
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

// Mockito нужен, чтобы задавать цены зависимостей.
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/*
 * @RunWith(Parameterized.class) говорит JUnit 4,
 * что этот класс должен выполняться как набор
 * параметризованных тестов.
 *
 * JUnit создаст отдельный экземпляр тестового класса
 * для каждого набора входных данных.
 */
@RunWith(Parameterized.class)
public class BurgerParameterizedTest {

    /*
     * Цена одной булочки.
     *
     * Это входной параметр теста, который отличается
     * в зависимости от выбранного набора данных.
     */
    private final float bunPrice;

    /*
     * Массив цен ингредиентов.
     *
     * Массив позволяет проверять бургеры:
     * - без ингредиентов;
     * - с одним ингредиентом;
     * - с несколькими ингредиентами.
     */
    private final float[] ingredientPrices;

    /*
     * Ожидаемая итоговая стоимость.
     *
     * Это эталонное значение, с которым сравним
     * результат burger.getPrice().
     */
    private final float expectedPrice;

    /*
     * Конструктор получает параметры из метода getParameters().
     *
     * JUnit 4 вызывает его отдельно для каждого набора данных.
     * Полученные значения сохраняются в полях класса,
     * чтобы тестовый метод мог использовать их.
     */
    public BurgerParameterizedTest(
            float bunPrice,
            float[] ingredientPrices,
            float expectedPrice
    ) {
        this.bunPrice = bunPrice;
        this.ingredientPrices = ingredientPrices;
        this.expectedPrice = expectedPrice;
    }

    /*
     * @Parameters сообщает JUnit 4, где получить
     * наборы входных данных для параметризации.
     *
     * Каждый Object[] содержит три параметра:
     * 1. Цена одной булочки.
     * 2. Массив цен ингредиентов.
     * 3. Ожидаемая итоговая стоимость бургера.
     *
     * Каждый набор будет использован при отдельном запуске
     * метода getPriceReturnsExpectedPrice().
     */
    @Parameterized.Parameters
    public static Object[][] getParameters() {
        return new Object[][]{
                // Булочка стоит 100, ингредиентов нет.
                // Цена: 100 * 2 = 200.
                {100f, new float[]{}, 200f},

                // Булочка стоит 100, один ингредиент стоит 50.
                // Цена: 100 * 2 + 50 = 250.
                {100f, new float[]{50f}, 250f},

                // Булочка стоит 100, два ингредиента стоят 50 и 30.
                // Цена: 100 * 2 + 50 + 30 = 280.
                {100f, new float[]{50f, 30f}, 280f},

                // Булочка стоит 200, ингредиенты — 100 и 50.
                // Цена: 200 * 2 + 100 + 50 = 550.
                {200f, new float[]{100f, 50f}, 550f},

                // Булочка стоит 300, ингредиенты — 100, 200 и 300.
                // Цена: 300 * 2 + 100 + 200 + 300 = 1200.
                {300f, new float[]{100f, 200f, 300f}, 1200f}
        };
    }

    /*
     * Параметризованный тест расчёта стоимости бургера.
     *
     * JUnit выполнит этот метод пять раз, используя
     * пять наборов данных из getParameters().
     */
    @Test
    public void getPriceReturnsExpectedPrice() {
        // Arrange: создаём новый бургер для текущего набора данных.
        Burger burger = new Burger();

        // Создаём мок булочки.
        Bun bun = mock(Bun.class);

        // Настраиваем стаб: возвращаем заданную цену булочки.
        when(bun.getPrice()).thenReturn(bunPrice);

        // Устанавливаем булочку в бургер.
        burger.setBuns(bun);

        /*
         * Создаём отдельный мок для каждого ингредиента.
         *
         * Для каждого мока настраиваем getPrice():
         * он должен возвращать цену из текущего массива.
         *
         * Затем добавляем ингредиент в бургер.
         *
         * Если массив пустой, цикл не выполнится.
         * В таком случае бургер останется без ингредиентов.
         */
        for (float ingredientPrice : ingredientPrices) {
            Ingredient ingredient = mock(Ingredient.class);

            when(ingredient.getPrice()).thenReturn(ingredientPrice);

            burger.addIngredient(ingredient);
        }

        // Act: рассчитываем стоимость бургера.
        float actualPrice = burger.getPrice();

        // Assert: сравниваем фактическую стоимость
        // с ожидаемой стоимостью из текущего набора данных.
        assertEquals(
                expectedPrice,
                actualPrice,
                0.001f
        );
    }
}