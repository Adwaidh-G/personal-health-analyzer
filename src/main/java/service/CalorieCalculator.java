package service;

import model.NutritionRecommendation;
import model.User;
import dao.DataManager;

public class CalorieCalculator {

    public enum BMICategory {
        UNDERWEIGHT, NORMAL, OVERWEIGHT
    }

    public BMICategory getBMICategory(double bmi) {
        if (bmi < 18.5)
            return BMICategory.UNDERWEIGHT;
        else if (bmi < 25)
            return BMICategory.NORMAL;
        else
            return BMICategory.OVERWEIGHT;
    }

    public NutritionRecommendation getRecommendation(User user) {
        double bmi = user.calculateBMI();
        BMICategory category = getBMICategory(bmi);
        return DataManager.getNutritionRecommendation(category.name());
    }
}
