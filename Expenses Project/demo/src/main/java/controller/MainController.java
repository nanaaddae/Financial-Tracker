package controller;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import transaction.ExpenseService;
import transaction.Expenses;
import users.Users;
import users.UsersService;

@Controller
public class MainController 
{

    @Autowired
	 private UsersService userService;

     @Autowired
	 private ExpenseService expenseService;

    @GetMapping("/register")
	    public String showRegisterPage() {
	        return "register";
	    }

        @GetMapping("/addExpense")
public String showAddExpensePage() {
    return "addExpense";
}

@PostMapping("/register")
public String registerUser(String firstName, String lastName, String email, String password, Model model) {
    try {
        userService.registerUser(email, password, firstName, lastName);
        model.addAttribute("successMessage", "Registration successful. Please log in.");
        return "index";
    } catch (Exception e) {
        model.addAttribute("errorMessage", e.getMessage());
        return "register";
    }
}

        @PostMapping("/login")
public ModelAndView loginUser(@RequestParam String email, @RequestParam String password, HttpServletRequest request) {
    ModelAndView modelAndView = new ModelAndView();
    Users user = userService.getUserByEmail(email);
    if (user == null || !user.getPassword().equals(password)) {
        modelAndView.addObject("error", "Invalid username or password");
        modelAndView.setViewName("index");
    } else {
        // login successful, set session attribute and redirect to home page
        modelAndView.addObject("user", user);
		 
        
        modelAndView.addObject("user", user);
        modelAndView.setViewName("home");
        HttpSession session = request.getSession();
        session.setAttribute("loggedInUser", user);
    }
    return modelAndView;
}



@GetMapping("/home")
public String showHomePage(Model model, HttpSession session) {
    Users loggedInUser = (Users) session.getAttribute("loggedInUser");
    if (loggedInUser == null) {
        // If the user is not logged in, redirect to the login page
        return "redirect:/index";
    }
    
    // Add the logged in user's information to the model
    model.addAttribute("loggedInUser", loggedInUser);

    return "home";
}


@GetMapping("/expense")
public String showExpensePage(Model model, HttpSession session) {
    Users loggedInUser = (Users) session.getAttribute("loggedInUser");
    if (loggedInUser == null) {
        // If the user is not logged in, redirect to the login page
        return "redirect:/login";
    }

    // Fetch the expenses for the logged-in user from the database
    List<Expenses> expenses = expenseService.getExpensesByUser(loggedInUser);

    // Add the list of expenses to the model
    model.addAttribute("expenses", expenses);

    return "expense";
}

@PostMapping("/expense")
public ModelAndView addExpense(@RequestParam String title, @RequestParam double amount,
                               @RequestParam String category, @RequestParam String date , HttpSession session) {
    ModelAndView modelAndView = new ModelAndView();
    
    try {
        // Convert the date string to LocalDate format
        LocalDate expenseDate = LocalDate.parse(date);
        
        Users user = (Users) session.getAttribute("loggedInUser");


        // Create a new Expense object using the provided parameters
        Expenses expense = new Expenses(expenseDate, category, title, amount);
        
        expense.setUser(user);


        expenseService.saveExpense(expense);

     




        // Add logic to save the expense to the database or perform any other necessary operations
        
        // Set success message and redirect to the expense list page
        modelAndView.addObject("successMessage", "Expense added successfully");
        modelAndView.setViewName("addExpense");
    } catch (DateTimeParseException e) {
        // Handle invalid date format
        modelAndView.addObject("errorMessage", "Invalid date format. Please enter a valid date (yyyy-MM-dd).");
        modelAndView.setViewName("addExpense");
    }
    
    return modelAndView;
}
   


 @GetMapping("/categories")
    public String showCategoriesPage(Model model, HttpSession session) {
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            // If the user is not logged in, redirect to the login page
            return "redirect:/login";
        }

        List<Expenses> expenses = expenseService.getExpensesByUser(loggedInUser);

        List<Expenses> foodExpenses = new ArrayList<>();
    List<Expenses> transportationExpenses = new ArrayList<>();
    List<Expenses> essentialExpenses = new ArrayList<>();
    List<Expenses> entertainmentExpenses = new ArrayList<>();

    for (Expenses expense : expenses) {
        String category = expense.getCategory();

        switch (category) {
            case "Food":
                foodExpenses.add(expense);
                break;
            case "Transportation":
                transportationExpenses.add(expense);
                break;
            case "Essential":
                essentialExpenses.add(expense);
                break;
            case "Entertainment":
                entertainmentExpenses.add(expense);
                break;
        }
    }

    model.addAttribute("foodExpenses", foodExpenses);
    model.addAttribute("transportationExpenses", transportationExpenses);
    model.addAttribute("essentialExpenses", essentialExpenses);
    model.addAttribute("entertainmentExpenses", entertainmentExpenses);


        return "categories";
    }

 
    @GetMapping("/profile")
public String showProfilePage(Model model, HttpSession session) {
    Users loggedInUser = (Users) session.getAttribute("loggedInUser");
    if (loggedInUser == null) {
        // If the user is not logged in, redirect to the login page
        return "redirect:/login";
    }

    model.addAttribute("loggedInUser", loggedInUser);

    return "profile";
}


@GetMapping("/reports")
public String showReportsPage(Model model, HttpSession session) {
    Users loggedInUser = (Users) session.getAttribute("loggedInUser");
    if (loggedInUser == null) {
        // If the user is not logged in, redirect to the login page
        return "redirect:/login";
    }

    // Fetch the data for categories spent
    List<Expenses> expenses = expenseService.getExpensesByUser(loggedInUser);

    // Create a Map to store the expenses for each category
    Map<String, Double> expensesByCategory = new HashMap<>();

    // Loop through the expenses and sum the amounts for each category
    for (Expenses expense : expenses) {
        String category = expense.getCategory();

        // If the category is already in the Map, add the amount to the existing value
        if (expensesByCategory.containsKey(category)) {
            expensesByCategory.put(category, expensesByCategory.get(category) + expense.getAmount());
        } else {
            // If the category is not in the Map, create a new entry
            expensesByCategory.put(category, expense.getAmount());
        }
    }

    model.addAttribute("expensesByCategory", expensesByCategory);

    return "reports";
}

@GetMapping("/logout")
public String logout(HttpSession session) {
    // Invalidate the session to log out the user
    session.invalidate();
    return "index"; // You can redirect to any page after logout
}

}

