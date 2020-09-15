package db.calorietracker.calorietrackerapp.model;

import org.springframework.util.MultiValueMap;

import javax.persistence.*;

@Entity
@Table(name="calories")
public class Calories
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="cid")
    int id;
    @Column(name="item_name")
    String itemName;
    @Column(name="calorie_count")
    int calorieCount;
    @ManyToOne
    @JoinColumn(name="person_id")
    private User user;

    public Calories()
    {

    }

    public Calories(String itemName, int calorieCount, User user)
    {
        this.itemName = itemName;
        this.calorieCount = calorieCount;
        this.user = user;
    }

    public Calories(MultiValueMap<String, String> params, User user)
    {
        this.itemName = params.getFirst("name");
        this.calorieCount = Integer.parseInt(params.getFirst("calories"));
        this.user = user;
    }

    //this should only be called when calling an update on a row in the calories database
    public Calories(int id, MultiValueMap<String, String> params, User user)
    {
        this.id = id;
        this.itemName = params.getFirst("name");
        this.calorieCount = Integer.parseInt(params.getFirst("calories"));
        this.user = user;

    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getItemName()
    {
        return itemName;
    }

    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

    public int getCalorieCount()
    {
        return calorieCount;
    }

    public void setCalorieCount(int calorieCount)
    {
        this.calorieCount = calorieCount;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}
