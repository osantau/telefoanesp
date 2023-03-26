/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oct.soft.controller;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oct.soft.dao.ReportDao;
import oct.soft.dao.beans.BirouBean;
import oct.soft.dao.beans.PersonBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 *
 * @author osantau
 */
@Controller
public class HomeController {

    @Autowired
    private ReportDao reportDao;

    private final RequestMappingHandlerMapping handlerMapping;

    @Autowired
    public HomeController(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
    public String home(Model model) {
        return "home";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public @ResponseBody
    String search(HttpServletRequest request, Authentication authentication) {
        boolean userIsAuthenticated = authentication == null ? false : authentication.isAuthenticated();
        String keyword = (request.getParameter("keyword"));
        String basePath = request.getServletContext().getContextPath();
        StringBuilder sb = new StringBuilder();
        List<BirouBean> filiale = reportDao.searchBranch(keyword);
        List<BirouBean> birouri = reportDao.searchOffice(keyword);
        List<PersonBean> persoane = reportDao.searchPerson(keyword);
        if (!filiale.isEmpty()) {
            sb = new StringBuilder("<div align=\"center\"><table cellspacing=\"5px\">\r\n"
                    + "                         <thead>\r\n"
                    + "                         <th>Nr.<br />Crt.</th>\r\n"
                    + "                            <th>Filiala</th>\r\n"
                    + "                            <th>Telefon</th>\r\n"
                    + "                         </thead>\r\n"
                    + "                        <tbody>");
            int count = 1;
            for (BirouBean f : filiale) {
                sb.append("<tr><td>").append(count++)
                        .append("</td><td>").append(f.getName())
                        .append("</td><td align=\"center\"><b>")
                        .append(f.getNumber())
                        .append("</b></td>");

                if (userIsAuthenticated) {
                    sb.append("<td>")
                            .append("<a href=\"")
                            .append(basePath + "/branches/edit/" + f.getIdoffice()).append("\"")
                            .append(" title =\"Modifica\" style=\"text-decoration:none;\" >")
                            .append("<img src=\"" + basePath + "/resources/images/pencil.png\" border=\"0\"/>")
                            .append("</a>")
                            .append("</td>")
                            .append("</tr>");
                }
            }
            sb.append("</tbody></table>");
            return sb.toString();
        } else if (!birouri.isEmpty()) {
            sb = new StringBuilder("<div align=\"center\"><table cellspacing=\"5px\">\r\n"
                    + "                         <thead>\r\n"
                    + "                         <th>Nr.<br />Crt.</th>\r\n"
                    + "                            <th>Birou</th>\r\n"
                    + "                            <th>Filiala</th>\r\n"
                    + "                            <th>Telefon</th>\r\n"
                    + "                         </thead>\r\n"
                    + "                        <tbody>");

            int count = 1;
            for (BirouBean b : birouri) {
                sb.append("<tr>")
                        .append("<td>").append(count++).append("</td><td>").append(b.getName()).append("</td>")
                        .append("<td>").append(b.getBranch()).append("</td>")
                        .append("</td><td align=\"center\"><b>").append(b.getNumber()).append("</b></td>");

                if (userIsAuthenticated) {
                    sb.append("<td>")
                            .append("<a href=\"")
                            .append(basePath + "/offices/edit/" + b.getIdoffice()).append("\"")
                            .append(" title =\"Modifica\" style=\"text-decoration:none;\" >")
                            .append("<img src=\"" + basePath + "/resources/images/pencil.png\" border=\"0\"/>")
                            .append("</a>")
                            .append("</td>");
                }
                sb.append("</tr>");
            }
            sb.append("</tbody></table>");
            return sb.toString();
        } else if (!persoane.isEmpty()) {
            int maxNumePrenume = reportDao.maxNumePrenume(persoane) + 5;
            sb = new StringBuilder("<div align=\"center\"><table cellspacing=\"" + maxNumePrenume + "\" style=\"text-align: center;\"><tbody>");
            for (PersonBean p : persoane) {
                sb.append("<tr><td style=\"font-weight: bold; vertical-align: top; text-align: left;\">" + p.getFname() + " " + p.getLname() + "</td>");
                sb.append("<td>  <ul style=\"text-align: left; list-style-type: bullet;\">");
                sb.append("<li>").append(p.getBranch()).append(" - ").append(p.getName()).append(": <b>");
                for (String number : p.getNumber().split(",")) {
                    boolean isNumAllocated = reportDao.hasPersonNumber(p.getIdperson(), number, p.getIdoffice());

                    if (isNumAllocated) {
                        sb.append("<span class=\"nrint\" style=\"color: red;\">&nbsp;" + number + "&nbsp;<input type=\"hidden\" id=\"person_id\" value=\"" + p.getIdperson() + "\"/></span>&nbsp;");
                        if (userIsAuthenticated) {
                            sb.append("<a href=\"#\" class=\"remphone\" style=\"cursor:hand;\">[-]<input type=\"hidden\" id=\"remp_id\" value=\"" + p.getIdperson() + "\"/>\r\n"
                                    + "                                            <input type=\"hidden\" id=\"remnum\" value=\"" + number + "\"/></a>");
                        }
                    } else {
                        if (userIsAuthenticated) {
                            sb.append("<span class=\"nrint\"><a href=\"#\" style=\"cursor:hand; text-decoration:none\">" + number + "</a><input type=\"hidden\" id=\"person_id\" value=\"" + p.getIdperson() + "\"/>"
                                    + "<input type=\"hidden\" id=\"office_id\" value=\"" + p.getIdoffice() + "\"/></span>&nbsp;");
                        } else {
                            sb.append("&nbsp;&nbsp;").append(number).append("&nbsp;&nbsp;");
                        }
                    }

                }
                sb.append("</b></li>");

                if (p.getTelserv() != null && !p.getTelserv().isEmpty()) {
                    sb.append("<li>Tel serv: <b>" + p.getTelserv() + "</b></li>");
                } else if (p.getTelfix() != null && !p.getTelfix().isEmpty()) {
                    sb.append("<li>Tel fix: <b>" + p.getTelfix() + "</b></li>");
                } else if (p.getTelmobil() != null && !p.getTelmobil().isEmpty()) {
                    sb.append("<li>Tel mobil: <b>" + p.getTelmobil() + "</b></li>");
                }

                sb.append("</ul></td>");

                if (userIsAuthenticated) {
                    sb.append("<td><a href=\"").append(basePath).append("/persons/edit/" + p.getIdperson() + "")
                            .append("\" title=\"Modifica\" style=\"text-decoration:none;\">")
                            .append("<img src=\"").append(basePath).append("/resources/images/pencil.png\" border=\"0\"/>")
                            .append("</a></td>");

                }
                sb.append("</tr>");
            }

            sb.append("</tbody></table>");
            // javascript functionality
            sb.append("<input type=\"hidden\" id=\"base_url\" value=\"" + basePath + "\"/>");
            sb.append("<div id=\"info\" style=\"text-weight: bolder;\"></div>");
            if (userIsAuthenticated) {
                sb.append("<script type=\"text/javascript\">   \r\n"
                        + "                    $(\".remphone\").click(function(e){\r\n"                      
                        + "                    var remp_id=$(this).find(\"#remp_id\").val();\r\n"
                        + "                    var remnum=$(this).find(\"#remnum\").val();\r\n"
                        + "                    var base_url=\""+basePath+"/rempersint\";  \r\n"
                        + "                    var base_url2=\""+basePath+"/search\";\r\n"
                        + "                    var term=$(\"#faq_search_input\").val();\r\n"
                        + "                            $.post(\r\n"
                        + "                                base_url,{remnum: remnum, rempers_id: remp_id},function(response){\r\n"
                        + "                              //  $(\"#info\").html(response).show();\r\n"
                        + "                                          }\r\n"
                        + "                                );\r\n"
                        + "                            $.ajax({\r\n"
                        + "                                            type: \"POST\",\r\n"
                        + "                                            url: base_url2,\r\n"
                        + "                                            data: \"keyword=\"+term,\r\n"
                        + "                                            success: function(response){\r\n"
                        + "                                                        $(\".gbox\").html(response).show();\r\n"
                        + "\r\n"
                        + "                                                                            }   \r\n"
                        + "                                        });\r\n"
                        + "                               \r\n"
                        + "                     });\r\n"
                        + "                    $(\".nrint\").click(function(){\r\n"
                        + "                           var person_id=$(this).find(\"#person_id\").val();\r\n"
                        + "                           var office_id=$(this).find(\"#office_id\").val();\r\n"
                        + "                           var numar=$(this).text();\r\n"
                        + "                           var base_url=$(\"#base_url\").val()+\"/persnrint\";  \r\n"
                        + "                           var base_url2=$(\"#base_url\").val()+\"/search\";\r\n"
                        + "                           var term=$(\"#faq_search_input\").val();\r\n"
//                        + "                           console.log(numar,person_id, office_id);"
                        + "                             $.post(\r\n"
                        + "                                base_url,{numar: numar, person_id: person_id, office_id: office_id},function(response){\r\n"
                        + "                              //  $(\"#info\").html(response).show();\r\n"
                        + "                                                    }\r\n"
                        + "                                );\r\n"
                        + "                       $.ajax({\r\n"
                        + "                                            type: \"POST\",\r\n"
                        + "                                            url: base_url2,\r\n"
                        + "                                            data: \"keyword=\"+term,\r\n"
                        + "                                            success: function(response){\r\n"
                        + "                                                        $(\".gbox\").html(response).show();\r\n"
                        + "\r\n"
                        + "                                                                            }   \r\n"
                        + "                                        });           \r\n"
                        + "                              });                                            \r\n"
                        + "                    </script>");
            }
            return sb.toString();
        }
        return sb.toString();
    }

    @RequestMapping(value = {"/reports/{type}"}, method = RequestMethod.GET)
    public String export(@PathVariable("type") String type, HttpServletResponse response) {
        if (Arrays.asList("filiale", "birouri", "personal").contains(type)) {
            response.reset();
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Expires:", "0"); // eliminates browser caching
        }
        if (type.equals("filiale")) {
            response.setHeader("Content-Disposition", "inline; attachment; filename=Filiale.xlsx");
            // Captured backflow
            try {
                byte[] outArray = reportDao.reportFiliale().toByteArray();
                response.setContentLength(outArray.length);
                OutputStream out = response.getOutputStream();
                out.write(outArray);
                out.flush();        // We emptied the flow
                out.close();        // We close the flow
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else if (type.equals("birouri")) {
            response.setHeader("Content-Disposition", "inline; attachment; filename=Birouri.xlsx");
            // Captured backflow
            try {
                byte[] outArray = reportDao.reportBirouri().toByteArray();
                response.setContentLength(outArray.length);
                OutputStream out = response.getOutputStream();
                out.write(outArray);
                out.flush();        // We emptied the flow
                out.close();        // We close the flow
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else if (type.equals("personal")) {
            response.setHeader("Content-Disposition", "inline; attachment; filename=Personal.xlsx");
            // Captured backflow
            try {
                byte[] outArray = reportDao.reportPersonal().toByteArray();
                response.setContentLength(outArray.length);
                OutputStream out = response.getOutputStream();
                out.write(outArray);
                out.flush();        // We emptied the flow
                out.close();        // We close the flow
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return "export";

    }

    @RequestMapping(value = "/rempersint", method = RequestMethod.POST)
    public @ResponseBody void removePersonInternalPhone(HttpServletRequest request) {        
        reportDao.removePersonInt(request.getParameter("remnum"), Integer.valueOf(request.getParameter("rempers_id")));
    }

    @RequestMapping(value = "/persnrint", method = RequestMethod.POST)
    public  @ResponseBody void addPersonInternalPhone(HttpServletRequest request) {
       String numar = request.getParameter("numar").replaceAll("[^0-9.]", "").trim();
       Integer idperson = Integer.valueOf(request.getParameter("person_id"));
       Integer idoffice = Integer.valueOf(request.getParameter("office_id"));
       reportDao.addPersonInt(numar, idperson,idoffice);
    }
}
