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
import oct.soft.dao.PhoneDao;
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
public class OfficeController {

    @Autowired
    private OfficeBranchDao obd;

    @Autowired
    private PhoneDao phoneDao;

    @Value("${PAGESIZE}")
    private Integer PAGESIZE;

    @RequestMapping(value = {"/offices/add"}, method = RequestMethod.GET)
    public String form(Model model) {
        model.addAttribute("branches", obd.all(true));
        model.addAttribute("officeForm", new Office());
        return "office/form";
    }

    @RequestMapping(value = {"/offices/add"}, method = RequestMethod.POST)
    public String create(@ModelAttribute("officeForm") @Valid Office officeForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("branches", obd.all(true));
            return "office/form";
        }
        obd.create(officeForm, false);
        return "redirect:/offices";
    }

    @RequestMapping(value = {"/offices/edit/{id}"}, method = RequestMethod.GET)
    public String edit(@PathVariable("id") Integer id, Model model) {
        Office office = obd.findById(id, false);
        model.addAttribute("branches", obd.all(true));
        model.addAttribute("officeForm", office);
        model.addAttribute("parentSelected", office.getParent());
        model.addAttribute("action", "edit");
        model.addAttribute("phones", phoneDao.phoneList(id, 0));
        return "office/form";
    }

    @RequestMapping(value = {"/offices/addphone/{id}"}, method = RequestMethod.POST)
    public String addPhone(@PathVariable("id") Integer id, HttpServletRequest request) {
        Phone phone = new Phone();
        phone.setNumber(request.getParameter("number"));
        phone.setIdoffice(id);
        phoneDao.saveUpdate(phone, request.getParameter("tel"));
        return "redirect:/offices/edit/" + id;
    }

    @RequestMapping(value = {"/offices/deletephone/{idphone}/{idoffice}"}, method = RequestMethod.GET)
    public String removePhone(@PathVariable("idphone") Integer idphone, @PathVariable("idoffice") Integer idoffice) {
        phoneDao.removePhone(idphone, idoffice,0);
        return "redirect:/offices/edit/" + idoffice;
    }

    @RequestMapping(value = {"/offices", "/offices/{type}"}, method = RequestMethod.GET)
    public String list(Model model, @PathVariable Map<String, String> pathVariablesMap, HttpServletRequest request) {
        PagedListHolder<Office> officeList = null;
        MutableSortDefinition msd = new MutableSortDefinition("name", true, true);
        String type = pathVariablesMap.get("type");
        if (type == null) {
            officeList = new PagedListHolder<Office>();
            officeList.setSource(obd.all(false));
            officeList.setPageSize(PAGESIZE);
            officeList.setSort(msd);
            officeList.resort();
            request.getSession().setAttribute("officeList", officeList);

        } else if (type.equals("next")) {
            officeList = (PagedListHolder<Office>) request.getSession().getAttribute("officeList");
            officeList.nextPage();
        } else if (type.equals("prev")) {
            officeList = (PagedListHolder<Office>) request.getSession().getAttribute("officeList");
            officeList.previousPage();
        } else {
            officeList = (PagedListHolder<Office>) request.getSession().getAttribute("officeList");
            int pageNum = Integer.parseInt(type);
            officeList.setPage(pageNum);
        }
        return "office/list";
    }
}
