package ru.ibs.qa.steps;

import ru.ibs.objects.Product;
import ru.ibs.utils.UtilsProducts;

import io.cucumber.java.After;
import io.cucumber.java.ru.Допустим;
import io.cucumber.java.ru.И;

import org.junit.jupiter.api.Assertions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AddProductUISteps {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String TABLE_PRODUCT = "//table[@class='table']//tbody//tr";
    private static final String BUTTON_ADD_PRODUCT = "//button[contains(text(),'Добавить')]";
    private static final String BUTTON_SAVE_PRODUCT = "save";
    private static final String FORM_ADDED_PRODUCT = "modal-content";
    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String CHECKBOX_EXOTIC = "exotic";

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Допустим("^Пользователь открыл браузер (Chrome|Firefox)$")
    public void userOpenBrowser(String browser) {
        if (browser.equals("Chrome")) {
            driver = new ChromeDriver();
        } else {
            driver = new FirefoxDriver();
        }
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        wait = new WebDriverWait(driver, Duration.ofSeconds(1, 15));
    }

    @И("Пользователь открыл страницу по адресу {string}")
    public void userOpenProductPage(String url) {
        driver.get(url);
    }

    @И("^Пользователь нажимает на \"(Добавить|Сохранить)\"$")
    public void userClickButtonAddNewProduct(String nameButton) {

        if (nameButton.equals("Добавить")) {
            driver.findElement(By.xpath(BUTTON_ADD_PRODUCT)).click();
        } else {
            // Сохраняем товар
            driver.findElement(By.id(BUTTON_SAVE_PRODUCT)).click();
        }
    }

    @И("^Форма добавления товара \"(Закрылась|Открылась)\"$")
    public void checkVisibilityFormAddProduct(String string) {

        if (string.equals("Закрылась")) {
            // Проверяем что форма добавления товара открылась
            Assertions.assertTrue(
                    wait.until(ExpectedConditions.invisibilityOf(
                            driver.findElement(By.className(FORM_ADDED_PRODUCT)))),
                    "Окно добавления товара не закрылось"
            );
        } else {
            // Провеярем, что форма добавления товара закрылась
            Assertions.assertTrue(
                    wait.until(ExpectedConditions.visibilityOf(
                            driver.findElement(By.className(FORM_ADDED_PRODUCT)))).isDisplayed(),
                    "Окно добавления товара не открылось"
            );
        }
    }

    @И("Заполняются поля товара")
    public void userSetValueFormAddProduct(Map<String, String> dataTable) {
        driver.findElement(By.name(NAME)).sendKeys(dataTable.get("Имя"));

        Select selectType = new Select(driver.findElement(By.id(TYPE)));
        selectType.selectByVisibleText(dataTable.get("Тип"));

        WebElement checkbox = driver.findElement(By.id(CHECKBOX_EXOTIC));
        boolean exoticProduct = Boolean.parseBoolean(dataTable.get("Экзотический"));

        if ((exoticProduct && !checkbox.isSelected())
                || (!exoticProduct && checkbox.isSelected())) {
            checkbox.click();
        }
    }

    @И("Значения полей заполнены значениями")
    public void checkValueFormAddProduct(Map<String, String> dataTable) {
        // Проверяем правильность заполнения формы, проверяя все поля
        Assertions.assertAll(
                () -> Assertions.assertEquals(
                        dataTable.get("Имя"),
                        driver.findElement(By.name(NAME)).getAttribute("value"),
                        "Неверно указано поле 'Наименование' в форме"),
                () -> Assertions.assertEquals(
                        dataTable.get("Тип"),
                        new Select(driver.findElement(By.id(TYPE))).getFirstSelectedOption().getText(),
                        "Неверно указано поле 'Тип' в форме"),
                () -> Assertions.assertEquals(
                        Boolean.parseBoolean(dataTable.get("Экзотический")),
                        driver.findElement(By.id(CHECKBOX_EXOTIC)).isSelected(),
                        "Неверно указано поле 'Экзотический' в форме")
        );
    }

    @И("В таблице пользователь видит новый товар")
    public void renamePls(Map<String, String> dataTable) {

        // Выгружаем список товаров из таблицы
        List<Product> productsListAfter = UtilsProducts.getParseListProduct(driver.findElements(By.xpath(TABLE_PRODUCT)));

        // Проверяем последний товар. И Проверяем все поля
        Product productCheck = productsListAfter.get(productsListAfter.size() - 1);

        Assertions.assertAll(
                () -> Assertions.assertEquals(
                        dataTable.get("Имя"), productCheck.getName(),
                        "Неверно указано поле 'Наименование'"),
                () -> Assertions.assertEquals(
                        dataTable.get("Тип"), productCheck.getType(),
                        "Неверно указано поле 'Тип'"),
                () -> Assertions.assertEquals(
                        Boolean.parseBoolean(dataTable.get("Экзотический")), productCheck.isExotic(),
                        "Неверно указано поле 'Экзотический'")
        );

        // Проверяем что id у товаров уникальные.
        Assertions.assertTrue(UtilsProducts.isUniqueListIndex(productsListAfter), "Товары не получили уникальный ID");
    }


}
