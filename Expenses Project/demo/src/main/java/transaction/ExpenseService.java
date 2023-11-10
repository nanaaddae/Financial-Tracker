package transaction;

import java.util.List;

import org.springframework.stereotype.Service;

import users.Users;

@Service
public class ExpenseService 
{
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) 
    {
        this.expenseRepository = expenseRepository;
    }

    public void saveExpense(Expenses expense) {
        // Add any additional business logic or validation here
        expenseRepository.save(expense);
    }

     public List<Expenses> getExpensesByUser(Users user) 
     {
        return expenseRepository.findByUser(user);
    }
}
