/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oct.soft.controller;

import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import oct.soft.dao.UserInfoDao;
import oct.soft.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author osantau
 */
@Controller
public class UsersController {

    @Autowired
    private UserInfoDao uiDao;

    @Value("${PAGESIZE}")
    private Integer PAGESIZE;

    @Value("${ROLES}")
    private String ROLES;

    @RequestMapping(value = "/utilizatori/adauga", method = RequestMethod.GET)
    public String userAddForm(Model model) {
        model.addAttribute("userForm", new UserInfo());
        model.addAttribute("roles", Arrays.asList(ROLES.split(",")));
        return "user/add-edit";
    }

    @RequestMapping(value = "/utilizatori/adauga", method = RequestMethod.POST)
    public String userAddForm(@ModelAttribute("userForm") @Valid UserInfo userInfo, BindingResult result, HttpServletRequest request, Model model) {
        String isEdit = request.getParameter("isEdit");
        if (uiDao.findUserInfo(userInfo.getUsername()) != null && isEdit==null) {
            result.addError(new ObjectError("username", "Utilizatorul " + userInfo.getUsername() + " exista! Acesta trebuie sa fie unic!"));
        }

        if (result.hasErrors()) {
            model.addAttribute("roles", Arrays.asList(ROLES.split(",")));
            return "user/add-edit";
        }
        String role = request.getParameter("role");
        if(isEdit!= null && isEdit.equals("edit"))
        {
            userInfo.setIduser(Integer.valueOf(request.getParameter("iduser")));
        }
        uiDao.createOrUpdate(userInfo, role);
        return "redirect:/utilizatori";
    }

    @RequestMapping(value = "/utilizatori/edit/{id}", method = RequestMethod.GET)
    public String userEditForm(@PathVariable("id") Integer id, Model model) {

        UserInfo userInfo = uiDao.getById(id);     
        model.addAttribute("userForm", userInfo);
        model.addAttribute("roles", Arrays.asList(ROLES.split(",")));
        model.addAttribute("iduser",id);
        model.addAttribute("isEdit","edit");
        return "user/add-edit";
    }
   @RequestMapping(value = "/utilizatori/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Integer id, Model model) {
        uiDao.delete(id);
        return "redirect:/utilizatori";
    }
    @RequestMapping(value = {"/utilizatori", "/utilizatori/{type}"}, method = RequestMethod.GET)
    public String list(Model model, @PathVariable Map<String, String> pathVariablesMap, HttpServletRequest request) {
        PagedListHolder<UserInfo> usersList = null;
        MutableSortDefinition msd = new MutableSortDefinition("username", true, true);
        String type = pathVariablesMap.get("type");
        if (type == null) {
            usersList = new PagedListHolder<UserInfo>();
            usersList.setSource(uiDao.all());
            usersList.setPageSize(PAGESIZE);
            usersList.setSort(msd);
            usersList.resort();
            request.getSession().setAttribute("usersList", usersList);

        } else if (type.equals("next")) {
            usersList = (PagedListHolder<UserInfo>) request.getSession().getAttribute("usersList");
            usersList.nextPage();
        } else if (type.equals("prev")) {
            usersList = (PagedListHolder<UserInfo>) request.getSession().getAttribute("usersList");
            usersList.previousPage();
        } else {
            usersList = (PagedListHolder<UserInfo>) request.getSession().getAttribute("usersList");
            int pageNum = Integer.parseInt(type);
            usersList.setPage(pageNum);
        }
        model.addAttribute("rolesInfo", uiDao.rolesInfo());
        return "user/list";
    }

}
