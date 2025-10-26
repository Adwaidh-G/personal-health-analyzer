package service;

import dao.DataManager;
import dao.DataManager.MacroInfo;

import java.util.List;

public class IntakeCalculator {

    public static class TotalMacroIntake {
        public int totalCalories = 0;
        public double totalProtein = 0.0;
        public double totalFat = 0.0;
        public double totalCarbs = 0.0;
    }

    // Calculates total macros weighted by quantity from food list ["Food",quantityString]
    public static TotalMacroIntake calculateTotalMacros(List<String> foodQuantityList) {
        TotalMacroIntake totalIntake = new TotalMacroIntake();

        for (String entry : foodQuantityList) {
            String[] parts = entry.split(",");
            if (parts.length != 2) continue;

            String foodName = parts[0].trim();

            int quantity = 1; // default qty
            try {
                quantity = Integer.parseInt(parts[1].trim());
                if (quantity < 1) quantity = 1;
            } catch (NumberFormatException e) {
                quantity = 1; // fallback qty
            }

            MacroInfo info = DataManager.getMacroInfoByFoodName(foodName);

            if (info != null) {
                totalIntake.totalCalories += info.calories * quantity;
                totalIntake.totalProtein += info.protein * quantity;
                totalIntake.totalFat += info.fat * quantity;
                totalIntake.totalCarbs += info.carbs * quantity;
            }
        }
        return totalIntake;
    }
}
