package ru.ibs.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.openqa.selenium.WebElement;
import ru.ibs.objects.Product;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

public class UtilsProducts {
    private static final String PATH_TO_PROPERTIES_BD = "src/test/resources/db.properties";

    public static List<Product> getParseListProduct(List<WebElement> listWebElement) {
        List<Product> resultProductList = new ArrayList<>();
        listWebElement.forEach(element -> {
            String[] tmpString = element.getText().split(" ");
            resultProductList.add(new Product(
                    Integer.parseInt(tmpString[0]),
                    tmpString[1],
                    tmpString[2],
                    Boolean.parseBoolean(tmpString[3])));
        });
        return resultProductList;
    }

    public static boolean isUniqueListIndex(List<Product> products) {
        return products.size() == new HashSet<>(products).size();
    }

    public static HikariDataSource getDataSourceHikari() {
        Properties properties = new Properties();
        HikariConfig config = new HikariConfig();
        try {
            properties.load(new FileInputStream(PATH_TO_PROPERTIES_BD));
            config.setJdbcUrl(properties.getProperty("db.url"));
            config.setUsername(properties.getProperty("db.username"));
            config.setPassword(properties.getProperty("db.password"));
        } catch (IOException e) {
            System.out.println("Ошибка в программе: файл " + PATH_TO_PROPERTIES_BD + " не обнаружен");
            e.printStackTrace();
        }
        return new HikariDataSource(config);
    }
}
