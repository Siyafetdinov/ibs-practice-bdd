package ru.ibs.dataBaseControl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.ibs.objects.Product;

public class DataBaseControl {
    private static JdbcTemplate jdbcTemplate;
    public DataBaseControl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public void selectAllFrom(String nameTable) {
        jdbcTemplate.execute("SELECT * FROM " + nameTable);
    }
    public Product getProduct(Product product) {
        String sql = "SELECT * FROM FOOD WHERE FOOD_NAME = ? AND FOOD_TYPE = ? AND FOOD_EXOTIC = ?";
        Object[] params = {product.getName(), product.getType(), product.isExotic()};
        RowMapper<Product> rowMapper = (resultSet, rowNum) -> new Product(
                resultSet.getString("FOOD_NAME"),
                resultSet.getString("FOOD_TYPE"),
                resultSet.getBoolean("FOOD_EXOTIC")
        );
        return jdbcTemplate.query(sql, params, rowMapper).stream().findFirst().orElse(null);
    }
    public void addProduct(Product product) {
        String sql = "INSERT INTO FOOD VALUES ((SELECT MAX(FOOD_ID) + 1 FROM FOOD), ?, ?, ?)";
        Object[] params = {product.getName(), product.getType(), product.isExotic()};
        jdbcTemplate.update(sql, params);
    }
    public void deleteProduct(Product product) {
        String sql = "DELETE FROM Food WHERE FOOD_NAME = ? AND FOOD_TYPE = ? AND FOOD_EXOTIC = ?";
        Object[] params = {product.getName(), product.getType(), product.isExotic()};
        jdbcTemplate.update(sql, params);
    }
}
