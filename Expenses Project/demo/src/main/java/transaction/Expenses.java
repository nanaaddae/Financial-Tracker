package transaction;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import users.Users;

@Entity
@Table(name = "Expenses")
public class Expenses 
{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "days")
    private LocalDate date;

    @Column(name = "category")
    private String category;


    @Column(name = "title")
    private String expenseName;

    @ManyToOne
    @JoinColumn(name = "user_id") // This is the foreign key column in the expenses table
    private Users user;
    
    


    private double amount;

     public Expenses() 
     {

    }

    public Expenses(LocalDate date, String category, String name, double amount) {
        this.date = date;
        this.category = category;
        this.expenseName = name;
        this.amount = amount;
    }


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    
    

}
