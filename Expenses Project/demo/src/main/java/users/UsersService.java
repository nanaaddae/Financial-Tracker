package users;

import org.springframework.stereotype.Service;

@Service
public class UsersService 
{
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository userRepository) {
        this.usersRepository = userRepository;
    }
    

    public void registerUser(String email, String password,String fname,String lname) {
        // Check if the user already exists
        if (usersRepository.existsByEmail(email) != false) {
            throw new RuntimeException("Username already exists");
        }

        // Create a new user
        Users user = new Users();
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName(fname);
        user.setLastName(lname);
        usersRepository.save(user);
    }
    
    public boolean authenticateUser(String email, String password) {
        Users user = usersRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return true; // Authentication successful
        }
        return false; // Authentication failed
    }

     public Users getUserByEmail(String email)
    {
        Users user = usersRepository.findByEmail(email);

        return user;
    }
}
