/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oct.soft.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import oct.soft.dao.OfficeBranchDao;
import oct.soft.model.Office;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import javax.validation.Valid;
import oct.soft.dao.PersonDao;
import oct.soft.dao.PhoneDao;
import oct.soft.model.Person;
import oct.soft.model.Phone;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author osantau
 */
@Controller
public class PersonController {

    @Autowired
    private PersonDao personDao;
    @Autowired
    private OfficeBranchDao obDao;
    @Autowired
    private PhoneDao phoneDao;
    
    @Value("${PAGESIZE}")
    private Integer PAGESIZE;

    @RequestMapping(value = {"/persons/add"}, method = RequestMethod.GET)
    public String form(Model model) {
        model.addAttribute("action", "add");
        model.addAttribute("personForm", new Person());
        return "person/form";
    }

    @RequestMapping(value = {"/persons/add"}, method = RequestMethod.POST)
    public String create(@ModelAttribute("personForm") @Valid Person person, BindingResult result) {
        if (result.hasErrors()) {
            return "person/form";
        }
        Person savedPerson = personDao.createOrUpdate(person);
        return "redirect:/persons/edit/"+savedPerson.getIdperson();
    }

    @RequestMapping(value = {"/persons/edit/{id}"}, method = RequestMethod.GET)
    public String edit(@PathVariable("id") Integer id, Model model) {
        Person person = personDao.getById(id);
        model.addAttribute("action", "edit");
        model.addAttribute("personForm", person);
        model.addAttribute("offices", obDao.getOfficesByPerson(id));
        model.addAttribute("officeCombo", obDao.getBranchOfficeChilds(id));
        model.addAttribute("phones", phoneDao.phoneList(0, id));
        
        return "person/form";
    }

    @RequestMapping(value = {"/persons/edit/{id}"}, method = RequestMethod.POST)
    public String editPost(@PathVariable("id") Integer id,@ModelAttribute("personForm") @Valid Person personForm, BindingResult result,Model model) {
        if(result.hasErrors())
        {
            return "person/form";
        }
        personForm.setIdperson(id);
        personDao.createOrUpdate(personForm);
        return "redirect:/persons";
    }

    @RequestMapping(value = {"/persons/{id}/add-office"}, method = RequestMethod.POST)
    public String editPostAddOffice(@PathVariable("id") Integer idperson, HttpServletRequest request) {
        personDao.addOfficeToPerson(idperson, Integer.valueOf(request.getParameter("idoffice")));
        return "redirect:/persons/edit/"+idperson;
    }
    
    @RequestMapping(value = {"/persons/remove-office/{idperson}/{idoffice}"}, method = RequestMethod.GET)
    public String editPostRemoveOffice(@PathVariable("idperson") Integer idperson, @PathVariable("idoffice") Integer idoffice) {
        personDao.removeOffice(idperson, idoffice);
        return "redirect:/persons/edit/"+idperson;
    }

    
    @RequestMapping(value = {"/persons/addphone/{id}"}, method = RequestMethod.POST)
    public String addPhone(@PathVariable("id") Integer id, HttpServletRequest request) {
        Phone phone = new Phone();
        phone.setNumber(request.getParameter("number"));
        phone.setIdperson(id);
        phoneDao.saveUpdate(phone, request.getParameter("tel"));
        return "redirect:/persons/edit/" + id;
    }

    @RequestMapping(value = {"/persons/deletephone/{idphone}/{idperson}"}, method = RequestMethod.GET)
    public String removePhone(@PathVariable("idphone") Integer idphone, @PathVariable("idperson") Integer idperson) {
        phoneDao.removePhone(idphone,0, idperson);
        return "redirect:/persons/edit/" + idperson;
    }
    @RequestMapping(value = {"/persons", "/persons/{type}"}, method = RequestMethod.GET)
    public String list(Model model, @PathVariable Map<String, String> pathVariablesMap, HttpServletRequest request) {
        PagedListHolder<Person> personList = null;
        MutableSortDefinition msd = new MutableSortDefinition("fname", true, true);
        String type = pathVariablesMap.get("type");
        if (type == null) {
            personList = new PagedListHolder<Person>();
            personList.setSource(personDao.all());
            personList.setPageSize(PAGESIZE);
            personList.setSort(msd);
            personList.resort();
            request.getSession().setAttribute("personList", personList);

        } else if (type.equals("next")) {
            personList = (PagedListHolder<Person>) request.getSession().getAttribute("personList");
            personList.nextPage();
        } else if (type.equals("prev")) {
            personList = (PagedListHolder<Person>) request.getSession().getAttribute("personList");
            personList.previousPage();
        } else {
            personList = (PagedListHolder<Person>) request.getSession().getAttribute("personList");
            int pageNum = Integer.parseInt(type);
            personList.setPage(pageNum);
        }
        return "person/list";
    }

    @RequestMapping(value = {"/persons/delete/{id}"}, method = RequestMethod.GET)
    public String delete(@PathVariable("id") Integer id) {
        personDao.deleteById(id);
        return "redirect:/persons";
    }
}
