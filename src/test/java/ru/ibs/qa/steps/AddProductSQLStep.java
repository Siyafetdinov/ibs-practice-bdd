package ru.ibs.qa.steps;

import io.cucumber.java.ru.Допустим;
import io.cucumber.java.ru.И;
import org.junit.jupiter.api.Assertions;
import ru.ibs.dataBaseControl.DataBaseControl;
import ru.ibs.objects.Product;

import java.util.Map;

public class AddProductSQLStep {
    private DataBaseControl dataBaseControl;

    @Допустим("пользователь подключен к БД")
    public void connectionDataBase() {
        dataBaseControl = DataBaseControl.getInstance();
    }

    @И("проверка таблица {string} существует в БД")
    public void checkTableExists(String nameTable) {
        dataBaseControl.selectAllFrom(nameTable);
    }

    @И("добавляем новый товар в БД")
    public void addProductToDatabase(Map<String, String> dataTable) {
        dataBaseControl.addProduct(new Product(dataTable.get("Имя"),
                dataTable.get("Тип"),
                Boolean.parseBoolean(dataTable.get("Экзотический"))));
    }

    @И("^проверка (отсутсвия|присутсвия) товара в БД$")
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

    @И("удаляем товар из БД")
    public void removedProductFromDatabase(Map<String, String> dataTable) {
        dataBaseControl.deleteProduct(
                new Product(
                        dataTable.get("Имя"),
                        dataTable.get("Тип"),
                        Boolean.parseBoolean(dataTable.get("Экзотический"))));
    }

}
