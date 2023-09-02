package ru.ibs.qa.steps;

import io.cucumber.java.ru.Допустим;
import io.cucumber.java.ru.И;
import org.junit.jupiter.api.Assertions;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.ibs.dataBaseControl.DataBaseControl;
import ru.ibs.objects.Product;
import ru.ibs.utils.UtilsProducts;

import java.util.Map;

public class AddProductSQLStep {
    protected DataBaseControl dataBaseControl;

    @Допустим("Пользователь подключен к БД")
    public void connectionDataBase() {
        dataBaseControl = new DataBaseControl(
                new JdbcTemplate(UtilsProducts.getDataSourceHikari()));
    }

    @И("Проверка таблица {string} существует в БД")
    public void checkTableExists(String nameTable) {
        dataBaseControl.selectAllFrom(nameTable);
    }

    @И("Добавляем новый товар в БД")
    public void addProductToDatabase(Map<String, String> dataTable) {
        dataBaseControl.addProduct(new Product(dataTable.get("Имя"),
                dataTable.get("Тип"),
                Boolean.parseBoolean(dataTable.get("Экзотический"))));
    }

    @И("^Проверка (отсутсвия|присутсвия) товара в БД$")
    public void checkProductInDatabase(String string, Map<String, String> dataTable) {
        Product product = new Product(
                dataTable.get("Имя"),
                dataTable.get("Тип"),
                Boolean.parseBoolean(dataTable.get("Экзотический")));

        if (string.equals("отсутсвия")) {
            Assertions.assertNull(
                    dataBaseControl.getProduct(product),
                    "Товар присуствует в БД");
        } else {
            Assertions.assertEquals(
                    product, dataBaseControl.getProduct(product),
                    "Товары не совпадают");
        }
    }

    @И("Удаляем товар из БД")
    public void removedProductFromDatabase(Map<String, String> dataTable) {
        dataBaseControl.deleteProduct(
                new Product(
                        dataTable.get("Имя"),
                        dataTable.get("Тип"),
                        Boolean.parseBoolean(dataTable.get("Экзотический"))));
    }

}
