package dao;

import db.DBConnection;
import model.NutritionRecommendation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataManager {

    public static NutritionRecommendation getNutritionRecommendation(String bmiCategory) {
        String query = "SELECT * FROM nutrition_recommendations WHERE UPPER(bmi_category) = UPPER(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, bmiCategory);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                NutritionRecommendation nr = new NutritionRecommendation();
                nr.setId(rs.getInt("id"));
                nr.setBmiCategory(rs.getString("bmi_category"));
                nr.setMinCalories(rs.getInt("min_calories"));
                nr.setMaxCalories(rs.getInt("max_calories"));
                nr.setProteinGrams(rs.getDouble("protein_grams"));
                nr.setFatGrams(rs.getDouble("fat_grams"));
                nr.setCarbGrams(rs.getDouble("carb_grams"));
                nr.setNotes(rs.getString("notes"));
                return nr;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class MacroInfo {
        public int calories;
        public double protein;
        public double fat;
        public double carbs;
    }

    public static MacroInfo getMacroInfoByFoodName(String foodName) {
        String query = "SELECT calories, protein, fat, carbs FROM food_nutrition WHERE food_name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, foodName.trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                MacroInfo info = new MacroInfo();
                info.calories = rs.getInt("calories");
                info.protein = rs.getDouble("protein");
                info.fat = rs.getDouble("fat");
                info.carbs = rs.getDouble("carbs");
                return info;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

