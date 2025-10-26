package model;

public class NutritionRecommendation {
    private int id;
    private String bmiCategory;
    private int minCalories;
    private int maxCalories;
    private double proteinGrams;
    private double fatGrams;
    private double carbGrams;
    private String notes;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBmiCategory() { return bmiCategory; }
    public void setBmiCategory(String bmiCategory) { this.bmiCategory = bmiCategory; }

    public int getMinCalories() { return minCalories; }
    public void setMinCalories(int minCalories) { this.minCalories = minCalories; }

    public int getMaxCalories() { return maxCalories; }
    public void setMaxCalories(int maxCalories) { this.maxCalories = maxCalories; }

    public double getProteinGrams() { return proteinGrams; }
    public void setProteinGrams(double proteinGrams) { this.proteinGrams = proteinGrams; }

    public double getFatGrams() { return fatGrams; }
    public void setFatGrams(double fatGrams) { this.fatGrams = fatGrams; }

    public double getCarbGrams() { return carbGrams; }
    public void setCarbGrams(double carbGrams) { this.carbGrams = carbGrams; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}

