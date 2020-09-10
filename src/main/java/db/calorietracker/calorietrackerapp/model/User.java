package db.calorietracker.calorietrackerapp.model;

import javax.persistence.*;

@Entity
@Table(name="user")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    int id;
    @Column(name="name")
    String name;
    @Column(name="age")
    int age;
    @Column(name="weight")
    int weight;
    @Column(name="height")
    int height;
    @Column(name="gender")
    char gender;
    @Column(name="activity_level")
    double activityLevel;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public int getWeight()
    {
        return weight;
    }

    public void setWeight(int weight)
    {
        this.weight = weight;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public char getGender()
    {
        return gender;
    }

    public void setGender(char gender)
    {
        this.gender = gender;
    }

    public double getActivityLevel()
    {
        return activityLevel;
    }

    public void setActivityLevel(double activityLevel)
    {
        this.activityLevel = activityLevel;
    }
}
