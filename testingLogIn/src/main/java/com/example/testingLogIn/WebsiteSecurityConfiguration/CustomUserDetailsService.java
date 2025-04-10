package com.example.testingLogIn.WebsiteSecurityConfiguration;

import com.example.testingLogIn.Enums.Role;
import com.example.testingLogIn.ModelDTO.UserDTO;
import com.example.testingLogIn.Models.AccountRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(11);

    public boolean restrictUser(int id) {
        try {
            UserModel user = getuser(id);
            if (user == null)
                return false;
            else {
                user.setNotRestricted(false);
                userRepo.save(user);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean unrestrictUser(int id) {
        try {
            UserModel user = getuser(id);
            if (user == null)
                return false;
            else {
                user.setNotRestricted(true);
                userRepo.save(user);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream()
                .map(UserModel::mapperDTO)
                .toList();
    }

    public UserModel getuser(int staffId) {
        return userRepo.findById(staffId).orElse(null);
    }

    public List<UserDTO> getTeachersAccount(){
        return userRepo.findAll().stream()
                          .filter(user -> user.isNotDeleted() && user.getRole().equals(Role.TEACHER))
                          .map(UserModel::mapperDTO)
                          .toList();
    }

    public boolean registerNewUser(AccountRegister accountRegister) {
        userRepo.save(AccountRegToUserModel(accountRegister));
        return true;
    }

    public boolean usernameExist(String username) {
        return userRepo.findByUsername(username) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        } else if (!user.isNotRestricted()) {
            throw new AccountStatusException("User is Currently Restricted") {
            };
        }
        return user;
    }

    public boolean isUsernameValid(String username) {
        return userRepo.findByUsername(username) != null;
    }

    public UserModel getCurrentlyLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
                return (UserModel)authentication.getPrincipal();
        }
        return null;
    }
    
    public UserDTO getCurrentlyLoggedInUserDTO(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
                return ((UserModel)authentication.getPrincipal()).mapperDTO();
        }
        return null;
    }

    private UserModel AccountRegToUserModel(AccountRegister accountRegister) {
        String fullname = accountRegister.getFirstname()+" "+ Optional.ofNullable(accountRegister.getMiddlename()).map(s-> s+" ").orElse("") + accountRegister.getLastname();
        return UserModel.builder()
                .isNotRestricted(true)
                .isNotDeleted(true)
                .firstname(accountRegister.getFirstname())
                .lastname(accountRegister.getLastname())
                .middlename(accountRegister.getMiddlename())
                .fullName(fullname)
                .role(accountRegister.getRole())
                .address(accountRegister.getAddress())
                .birthdate(accountRegister.getBirthdate())
                .gender(accountRegister.getGender())
                .username(accountRegister.getUsername())
                .password(encoder.encode(accountRegister.getPassword()))
                .build();
    } 
}
