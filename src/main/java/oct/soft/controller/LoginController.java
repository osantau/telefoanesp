/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oct.soft.controller;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import oct.soft.authentication.MyDBAuthenticationService;
import oct.soft.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author osantau
 */
@Controller
public class LoginController {
    
    @Autowired
    @Qualifier("authManager")
    private AuthenticationManager authManager;
    @Autowired
    private MyDBAuthenticationService authService;

    @RequestMapping(value = "/do-login", method = RequestMethod.GET)
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new UserInfo());
        return "user/login";
    }
 @RequestMapping(value = "/do-logout", method = RequestMethod.GET)
    public String processLogout(Model model) {       
        return "redirect:/";
    }
    @RequestMapping(value = "/process-login", method = RequestMethod.POST)
    public String processLogin(HttpServletRequest request, Model model) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UserDetails userDetails = authService.loadUserByUsername(username);
        boolean hasErrors = false;
        Map<String, String> errors = new HashMap<String, String>();
        if (userDetails == null) {
            hasErrors = true;
            errors.put("userNotExist", "Utilizatorul " + username + " nu exista!");
        } else {
            if (!userDetails.getPassword().equals(password)) {
                hasErrors = true;
                errors.put("wrongPassword", "Parola nu se potriveste cu utilizatorul!");
            }
        }
        if (hasErrors) {
            model.addAttribute("errors", errors);
            return "user/login";
        }
        UsernamePasswordAuthenticationToken autReq=new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = authManager.authenticate(autReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", sc);
        return "redirect:/";
    }
}
