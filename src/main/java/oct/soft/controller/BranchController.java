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
public class BranchController {

    @Autowired
    private OfficeBranchDao obd;

    @Autowired
    private PhoneDao phoneDao;
    
    @Value("${PAGESIZE}")
    private Integer PAGESIZE;

    @RequestMapping(value = {"/branches/add"}, method = RequestMethod.GET)
    public String form(Model model) {        
        model.addAttribute("branchForm", new Office());
        return "branch/form";
    }

    @RequestMapping(value = {"/branches/add"}, method = RequestMethod.POST)
    public String create(@ModelAttribute("branchForm") @Valid Office branchForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "branch/form";
        }
        obd.create(branchForm, true);
        return "redirect:/branches";
    }

    @RequestMapping(value = {"/branches/edit/{id}"}, method = RequestMethod.GET)
    public String edit(@PathVariable("id") Integer id, Model model)
    {        
        model.addAttribute("branchForm", obd.findById(id, true));
        model.addAttribute("action","edit");
        model.addAttribute("phones", phoneDao.phoneList(id, 0));
        return "branch/form";
    }
  
    @RequestMapping(value = {"/branches/addphone/{id}"}, method = RequestMethod.POST)
    public String addPhone(@PathVariable("id") Integer id, HttpServletRequest request)
    {   
        Phone phone = new Phone();
        phone.setNumber(request.getParameter("number"));     
        phone.setIdoffice(id);
        phoneDao.saveUpdate(phone, request.getParameter("tel"));        
        return "redirect:/branches/edit/"+id;
    }
    @RequestMapping(value = {"/branches/deletephone/{idphone}/{idoffice}"}, method = RequestMethod.GET)
    public String removePhone(@PathVariable("idphone") Integer idphone,@PathVariable("idoffice") Integer idoffice)
    {   
        phoneDao.removePhone(idphone,idoffice,0);       
        return "redirect:/branches/edit/"+idoffice;
    }
    @RequestMapping(value = {"/branches", "/branches/{type}"}, method = RequestMethod.GET)
    public String list(Model model, @PathVariable Map<String, String> pathVariablesMap, HttpServletRequest request) {
        PagedListHolder<Office> branchList = null;
        MutableSortDefinition msd = new MutableSortDefinition("name", true, true);
        String type = pathVariablesMap.get("type");
        if (type == null) {
            branchList = new PagedListHolder<Office>();
            branchList.setSource(obd.all(true));
            branchList.setPageSize(PAGESIZE);
            branchList.setSort(msd);
            branchList.resort();
            request.getSession().setAttribute("branchList", branchList);

        } else if (type.equals("next")) {
            branchList = (PagedListHolder<Office>) request.getSession().getAttribute("branchList");
            branchList.nextPage();
        } else if (type.equals("prev")) {
            branchList = (PagedListHolder<Office>) request.getSession().getAttribute("branchList");
            branchList.previousPage();
        } else {
            branchList = (PagedListHolder<Office>) request.getSession().getAttribute("branchList");
            int pageNum = Integer.parseInt(type);
            branchList.setPage(pageNum);
        }
        return "branch/list";
    }
}
