package praktikum;

// JUnit 4: аннотация, обозначающая тестовый метод.
import org.junit.Test;

// Статические методы JUnit для проверки результатов тестов.
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

// Mockito: создание моков, настройка поведения и проверка взаимодействий.
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

public class BurgerTest {

    /*
     * Тестируем метод setBuns().
     *
     * Задача метода — сохранить переданную булочку
     * в поле burger.bun.
     *
     * Mockito здесь не нужен: метод не обращается
     * к другим методам объекта Bun, а только сохраняет ссылку.
     */
    @Test
    public void setBunsSetsCorrectBun() {
        // Arrange: подготавливаем тестовые объекты.
        Burger burger = new Burger();
        Bun bun = new Bun("black bun", 100);

        // Act: выполняем проверяемое действие.
        burger.setBuns(bun);

        // Assert: проверяем, что сохранён именно тот объект,
        // который мы передали в метод.
        //
        // assertSame сравнивает ссылки на объекты,
        // а не только их содержимое.
        assertSame(bun, burger.bun);
    }

    /*
     * Тестируем метод addIngredient().
     *
     * Проверяем два условия:
     * 1. Размер списка ингредиентов увеличился до одного.
     * 2. В список добавлен именно переданный ингредиент.
     */
    @Test
    public void addIngredientAddsIngredientToList() {
        // Arrange: создаём бургер и реальный ингредиент.
        Burger burger = new Burger();

        Ingredient ingredient = new Ingredient(
                IngredientType.FILLING,
                "cutlet",
                100
        );

        // Act: добавляем ингредиент в бургер.
        burger.addIngredient(ingredient);

        // Assert: проверяем количество элементов в списке.
        assertEquals(1, burger.ingredients.size());

        // Проверяем, что сохранён тот же экземпляр ингредиента.
        assertSame(ingredient, burger.ingredients.get(0));
    }

    /*
     * Тестируем метод removeIngredient().
     *
     * Изначально список содержит два элемента:
     * [first, second]
     *
     * После removeIngredient(0) ожидаем:
     * [second]
     *
     * Проверяем и размер списка, и оставшийся элемент.
     */
    @Test
    public void removeIngredientRemovesIngredientByIndex() {
        // Arrange: создаём бургер и два ингредиента.
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

        // Добавляем элементы в определённом порядке.
        burger.addIngredient(first);
        burger.addIngredient(second);

        // Act: удаляем элемент с индексом 0.
        burger.removeIngredient(0);

        // Assert: в списке должен остаться один элемент.
        assertEquals(1, burger.ingredients.size());

        // Проверяем, что оставшимся элементом является second.
        assertSame(second, burger.ingredients.get(0));
    }

    /*
     * Тестируем метод moveIngredient().
     *
     * До перемещения:
     * [first, second, third]
     *
     * Вызов moveIngredient(0, 2) должен переместить
     * первый элемент на позицию с индексом 2.
     *
     * Ожидаемый порядок:
     * [second, third, first]
     *
     * Важно проверить весь список, а не только то,
     * что перемещённый элемент оказался в нужной позиции.
     */
    @Test
    public void moveIngredientChangesIngredientOrder() {
        // Arrange: создаём бургер и три ингредиента.
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

        // Добавляем ингредиенты в исходном порядке:
        // first, second, third.
        burger.addIngredient(first);
        burger.addIngredient(second);
        burger.addIngredient(third);

        // Act: переносим элемент с индекса 0 на индекс 2.
        burger.moveIngredient(0, 2);

        // Assert: проверяем каждый элемент нового списка.
        assertSame(second, burger.ingredients.get(0));
        assertSame(third, burger.ingredients.get(1));
        assertSame(first, burger.ingredients.get(2));
    }

    /*
     * Тестируем метод getPrice().
     *
     * Здесь применяем Mockito, потому что хотим проверить
     * именно расчёт цены в классе Burger.
     *
     * Нам не требуется тестировать реализацию getPrice()
     * внутри Bun и Ingredient. Мы задаём их цены заранее
     * с помощью стабов.
     *
     * Стаб (stub) — это настроенное поведение мока:
     * когда вызывается определённый метод, он возвращает
     * заранее заданное значение.
     */
    @Test
    public void getPriceReturnsCorrectBurgerPrice() {
        // Arrange: создаём бургер.
        Burger burger = new Burger();

        // Создаём моки зависимостей.
        // Это подставные объекты, поведение которых контролируем мы.
        Bun bun = mock(Bun.class);
        Ingredient first = mock(Ingredient.class);
        Ingredient second = mock(Ingredient.class);

        // Настраиваем стабы: задаём цены объектов.
        when(bun.getPrice()).thenReturn(100f);
        when(first.getPrice()).thenReturn(50f);
        when(second.getPrice()).thenReturn(30f);

        // Добавляем подготовленные объекты в бургер.
        burger.setBuns(bun);
        burger.addIngredient(first);
        burger.addIngredient(second);

        // Act: получаем итоговую стоимость бургера.
        float actualPrice = burger.getPrice();

        // Assert: проверяем расчёт.
        //
        // Формула:
        // цена двух булочек + цена первого ингредиента
        // + цена второго ингредиента.
        //
        // 100 * 2 + 50 + 30 = 280.
        //
        // Последний аргумент — допустимая погрешность
        // при сравнении значений типа float.
        assertEquals(280f, actualPrice, 0.001f);

        // Проверяем взаимодействие с моками.
        //
        // verify подтверждает, что Burger запросил цену
        // у булочки и обоих ингредиентов.
        verify(bun).getPrice();
        verify(first).getPrice();
        verify(second).getPrice();
    }

    /*
     * Тестируем метод getReceipt().
     *
     * Проверяем, что метод возвращает чек с правильными:
     * - названием булочки;
     * - типами ингредиентов;
     * - названиями ингредиентов;
     * - итоговой стоимостью.
     *
     * Моки позволяют заранее задать все значения,
     * которые Burger получает от Bun и Ingredient.
     */
    @Test
    public void getReceiptReturnsCorrectReceipt() {
        // Arrange: создаём бургер.
        Burger burger = new Burger();

        // Создаём моки булочки и двух ингредиентов.
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

        // Собираем бургер из подготовленных объектов.
        burger.setBuns(bun);
        burger.addIngredient(sauce);
        burger.addIngredient(filling);

        // Act: формируем чек.
        String actualReceipt = burger.getReceipt();

        // Assert: готовим ожидаемую строку.
        //
        // %n — перенос строки, соответствующий текущей ОС.
        // Цена выводится через %f с шестью знаками после запятой,
        // поэтому ожидаем именно 350.000000.
        //
        // Цена:
        // 100 * 2 + 50 + 100 = 350.
        String expectedReceipt = String.format(
                "(==== black bun ====)%n" +
                        "= sauce hot sauce =%n" +
                        "= filling cutlet =%n" +
                        "(==== black bun ====)%n" +
                        "%nPrice: %f%n", 350f);

        // Сравниваем весь чек целиком, включая переносы строк.
        assertEquals(expectedReceipt, actualReceipt);

        // Проверяем взаимодействие Burger с булочкой.
        //
        // getName() вызывается дважды:
        // один раз для верхней строки и один раз для нижней.
        verify(bun, times(2)).getName();

        // getPrice() вызывается при расчёте итоговой цены.
        verify(bun).getPrice();

        // Проверяем получение данных о соусе.
        verify(sauce).getType();
        verify(sauce).getName();
        verify(sauce).getPrice();

        // Проверяем получение данных о начинке.
        verify(filling).getType();
        verify(filling).getName();
        verify(filling).getPrice();
    }
}