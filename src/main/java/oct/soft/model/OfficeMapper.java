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
public class OfficeMapper implements RowMapper<Office> {

    @Override
    public Office mapRow(ResultSet rs, int i) throws SQLException {
       Office office = new Office();
       office.setIdoffice(rs.getInt("idoffice"));
       office.setName(rs.getString("name"));
       office.setIsbranch(rs.getInt("isbranch"));
       office.setParent(rs.getInt("parent"));
       return office;
    }
    
}
