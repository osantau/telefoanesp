/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oct.soft.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import oct.soft.model.Phone;
import oct.soft.model.PhoneMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 *
 * @author osantau
 */
@Repository
public class PhoneDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PhoneDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void saveUpdate(Phone phone, String type) {

        if (type.equals("interior")) {
            phone.setInterior(1);
        } else if (type.equals("fix")) {
            phone.setFix(1);
        } else if (type.equals("mobil")) {
            phone.setMobil(1);

        } else if (type.equals("serv")) {
            phone.setServ(1);
        }
        SimpleJdbcInsert sji = new SimpleJdbcInsert(jdbcTemplate).withTableName("phone");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("number", phone.getNumber());
        parameters.put("fix", phone.getFix());
        parameters.put("interior", phone.getInterior());
        parameters.put("mobil", phone.getMobil());
        parameters.put("serv", phone.getServ());
        parameters.put("idoffice", phone.getIdoffice());
        parameters.put("idperson", phone.getIdperson());
        sji.execute(parameters);
    }

    public List<Phone> phoneList(int idoffice, int idperson) {
        String sql = "SELECT * FROM phone WHERE ";
        if (idoffice > 0) {
            sql += "idoffice=" + idoffice;
        }
        if (idperson > 0) {
            sql += "idperson=" + idperson;
        }

        return jdbcTemplate.query(sql, new PhoneMapper());
    }

    public void removePhone(Integer idphone, Integer idoffice, Integer idperson) { 
        if(idoffice>0) {
        jdbcTemplate.update("DELETE FROM phone WHERE idphone=? AND idoffice=?", idphone, idoffice);
        }
        else if(idperson>0)
        {
            jdbcTemplate.update("DELETE FROM phone WHERE idphone=? AND idperson=?", idphone, idperson);
        }
    }
}
