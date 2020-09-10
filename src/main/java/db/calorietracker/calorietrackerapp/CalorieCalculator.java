package db.calorietracker.calorietrackerapp;

import db.calorietracker.calorietrackerapp.model.User;

public class CalorieCalculator
{

    public static double calculateDailyCalorieIntake(User user)
    {
        double dailyExpenditure = calculateBMR(user.getGender(), user.getWeight(), user.getHeight(), user.getAge());

        return dailyExpenditure * user.getActivityLevel();
    }

    private static double calculateBMR(char gender, int weight, int height, int age)
    {
        /*
            BMR Formula:

            Men: BMR = 66 + (6.23 x weight in pounds) + (12.7 x height in inches) - (6.8 x age in years)

            Women: BMR = 655 + (4.35 x weight in pounds) + (4.7 x height in inches) - (4.7 x age in years)
        */

        double BMR = 0.0;

        if(gender == 'm')
        {
            BMR = 66 + (6.23 * weight) + (12.7 * height) - (6.8 * age);
        }
        else
        {
            BMR = 655 + (4.35 * weight) + (4.7 * height) - (4.7 * age);
        }

        return BMR;
    }
}
