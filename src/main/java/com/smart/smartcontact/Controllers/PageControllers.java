package com.smart.smartcontact.Controllers;

import javax.servlet.http.HttpSession;


import com.smart.smartcontact.Repository.UserRepository;
import com.smart.smartcontact.entities.User;
import com.smart.smartcontact.helper.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageControllers{
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder; 

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/")
    public String homeHandler(Model model){
        model.addAttribute("title","Home | Smart Conact");
        return "home";
    }

    
    @RequestMapping("/about")
    public String aboutHandler(Model model){
        model.addAttribute("title","About | Smart Conact");
        return "about";
    }

    @RequestMapping("/signup")
    public String signupHandler(Model model){
        model.addAttribute("title","Signup | Smart Conact");
        model.addAttribute("user",new User());
        return "signup";
    }
    
    @PostMapping("/registerHandler")
    public String registerHandler( @ModelAttribute("user") User user, BindingResult result, @RequestParam(value = "agreement",defaultValue = "false") boolean agreement, Model model , HttpSession session){
        try{
           if(!agreement){
               System.out.println("You have not agree term and condition");
               throw new Exception("You have not agree term and condition");
           }
           if(result.hasErrors()){
               System.out.println("---------------------------------");
               System.out.print(result.toString());
               user.setPassword("");
               model.addAttribute("user", user);
               return "signup";
           }
           user.setEnabled(true);
           user.setImageURL("default.png");
           user.setRole("ROLE_USER");
           user.setPassword(passwordEncoder.encode(user.getPassword()));
           User re=userRepository.save(user);
           model.addAttribute("user",new User());
           session.setAttribute("message", new Message("Succefully register!!","alert-sucess"));
           System.out.println(re);
           System.out.println(agreement);
        }catch(Exception e){
            user.setPassword("");
            model.addAttribute("user", user);
            session.setAttribute("message", new Message(e.getMessage().toString() ,"alert-danger"));
            e.printStackTrace();
        }
        return "signup";
    }
     
    @RequestMapping("/signin")
    public String singin(Model model){
        model.addAttribute("title", "SignIn | Smart Contact");
        return "signin";
    }

    
}
