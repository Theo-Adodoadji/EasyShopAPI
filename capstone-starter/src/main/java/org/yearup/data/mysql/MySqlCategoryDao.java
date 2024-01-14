package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    @Autowired
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();

        try{
            String query = "SELECT category_id, name, description FROM categories";
            PreparedStatement statement = getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Category category = mapRow(resultSet);
                categories.add(category);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId) {
        Category category = null;
        try {
            String query = "SELECT category_id, name, description FROM categories WHERE category_id = ?";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, categoryId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                category = mapRow(resultSet);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return category;
    }

    @Override
    public Category create(Category category) {
        try {
            String query = "INSERT INTO categories(name, description) VALUES (?,?)";
            PreparedStatement statement = getConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new SQLException("Creating category failed, no rows affected.");
            }
            try(ResultSet generatedKeys = statement.getGeneratedKeys()){
                if (generatedKeys.next()){
                    category.setCategoryId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating category failed, no ID obtained.");
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return category;
    }

    @Override
    public void update(int categoryId, Category category) {
        try {
            String query = "UPDATE categories SET name = ?, description = ? WHERE category_id = ?";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int categoryId) {
        try{
            String query = "DELETE FROM categories WHERE category_id = ?";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, categoryId);
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setName(name);
        category.setDescription(description);

        return category;
    }

}
