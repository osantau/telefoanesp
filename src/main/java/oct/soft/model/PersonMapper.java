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
public class PersonMapper implements RowMapper<Person> {
    
    @Override
    public Person mapRow(ResultSet rs, int i) throws SQLException {
        Person person = new Person();
        person.setIdperson(rs.getInt("idperson"));
        person.setFname(rs.getString("fname"));
        person.setLname(rs.getString("lname"));
        person.setNickname(rs.getString("nickname"));
        person.setTelint_id(rs.getInt("telint_id"));     
        person.setBirou(rs.getString("birou"));
        person.setFiliala(rs.getString("filiala"));
        return person;
    }
    
}
