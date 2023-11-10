package transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import users.Users;


public interface ExpenseRepository extends JpaRepository<Expenses, Long> 
{
    
        List<Expenses> findByUser(Users user);


}
