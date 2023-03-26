/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package oct.soft.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author osantau
 */
public class PhoneMapper implements RowMapper<Phone> {
    
    @Override
    public Phone mapRow(ResultSet rs, int i) throws SQLException {
        Phone phone = new Phone();
        phone.setIdphone(rs.getInt("idphone"));
        phone.setNumber(rs.getString("number"));
        phone.setFix(rs.getInt("fix"));
        phone.setInterior(rs.getInt("interior"));
        phone.setFix(rs.getInt("fix"));
        phone.setInterior(rs.getInt("interior"));
        phone.setMobil(rs.getInt("mobil"));
        phone.setServ(rs.getInt("serv"));
        phone.setIdoffice(rs.getInt("idoffice"));
        phone.setIdperson(rs.getInt("idperson"));
        return phone;
    }
    
}
