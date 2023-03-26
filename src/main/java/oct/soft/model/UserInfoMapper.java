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
public class UserInfoMapper implements RowMapper<UserInfo>{

    @Override
    public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        
        return new UserInfo(rs.getInt("iduser"),rs.getString("username"),rs.getString("password"), rs.getInt("enabled"));        
    }
    
}
