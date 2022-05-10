package com.smart.smartcontact.Controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import com.smart.smartcontact.Repository.ContactRepository;
import com.smart.smartcontact.Repository.UserRepository;
import com.smart.smartcontact.entities.Contact;
import com.smart.smartcontact.entities.User;
import com.smart.smartcontact.helper.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class UserControllers {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @ModelAttribute
    public void addCommonData(Model model,Principal principal){
        String username=  principal.getName();
        User user= userRepository.getUserByUserEmail(username);
        model.addAttribute("user", user);
    }

    @RequestMapping("/index")
    public String dashboard(Model model,Principal principal){
        model.addAttribute("title", "DashBoard | Smart Contact");
        return "normal/dashboard";
    }
    
    @RequestMapping("/add-contact")
    public String addConact(Model model){
        model.addAttribute("title","Add Contact | Smart Contact");
        model.addAttribute("contact", new Contact());
        return "normal/addContact";
    }

    @PostMapping("/process-contact")
    public String processContatact(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file,Principal principal,Model model, HttpSession session){
        
        try{
            String username=principal.getName();
            User user=this.userRepository.getUserByUserEmail(username);
            if(file.isEmpty()){
                System.out.println("File is Empty");
                contact.setImageURL("contact.png");
            }else{
                String filename=file.getOriginalFilename();
                contact.setImageURL(filename);
                File savefile=new ClassPathResource("static/images").getFile();
                Path path=Paths.get(savefile.getAbsolutePath()+File.separator+filename);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File is uploded");
                
            }
            user.getContacts().add(contact);
            contact.setUser(user);
            this.userRepository.save(user);
            model.addAttribute("title", "Add Contact | Smart Contact");
            session.setAttribute("message", new Message("Contact Added successfully !! Add more Contact", "success"));
        }catch(Exception e){
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went worng !! try again", "danger"));
        }
        return "normal/addContact";
    }

    @GetMapping("/show-contact/{page}")
    public String showContact(@PathVariable("page") Integer page,Model model,Principal principal){
        model.addAttribute("title", "show contact | Smart Contact");
        String username=principal.getName();
        User user=this.userRepository.getUserByUserEmail(username);
        Pageable pageable= PageRequest.of(page, 8);
        Page<Contact> contacts=this.contactRepository.findContactsByUser(user.getUid(),pageable);
        model.addAttribute("contacts",contacts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage",contacts.getTotalPages());
        return "normal/showContact";
    }
    
    @RequestMapping("/{cid}/contact")
    public String showContactDetails(@PathVariable("cid") Integer cid,Model model,Principal principal){
        //System.out.print(cid);
        String username=principal.getName();
        User user=this.userRepository.getUserByUserEmail(username);
        Optional<Contact> optionalContact=this.contactRepository.findById(cid);
        Contact contact=optionalContact.get();
        if(user.getUid()==contact.getUser().getUid()){
          model.addAttribute("contact",contact);
          model.addAttribute("title", "conact details");
        }
        return "normal/showContactDetails";
    }
}
