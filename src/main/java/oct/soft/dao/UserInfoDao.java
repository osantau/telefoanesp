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
import oct.soft.model.UserInfo;
import oct.soft.model.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 *
 * @author osantau
 */
@Service
@Transactional
public class UserInfoDao extends JdbcDaoSupport {

    @Autowired
    public UserInfoDao(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public UserInfo findUserInfo(String userName) {
        String sql = "SELECT u.iduser, u.username, u.password,u.enabled FROM users u WHERE u.username=?";
        Object[] params = new Object[]{userName};
        try {
            UserInfo userInfo = this.getJdbcTemplate().queryForObject(sql, params, new UserInfoMapper());
            return userInfo;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<String> getUserRoles(String userName) {
        String sql = "Select r.authority "
                + " from authorities r where r.username = ? ";

        Object[] params = new Object[]{userName};

        List<String> roles = this.getJdbcTemplate().queryForList(sql, params, String.class);        
        return roles;
    }

    public List<UserInfo> all() {
        String sql = "SELECT * FROM users";
        return this.getJdbcTemplate().query(sql, new UserInfoMapper());
    }
    
    public Map<String, String> rolesInfo()
    {
        Map<String, String> result = new HashMap<String, String>();
        for(UserInfo ui : all())
        {
            result.put(ui.getUsername(), StringUtils.arrayToCommaDelimitedString(getUserRoles(ui.getUsername()).toArray()));
        }
        return result;
    }
    
    public UserInfo createOrUpdate(UserInfo userInfo, String role) {       
        if (userInfo.isNew()) {
            
            SimpleJdbcInsert sji = new SimpleJdbcInsert(this.getJdbcTemplate()).withTableName("users").usingGeneratedKeyColumns("iduser");
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("username", userInfo.getUsername());
            parameters.put("password", userInfo.getPassword());  
            parameters.put("enabled", userInfo.getEnabled());  
            int id = sji.executeAndReturnKey(parameters).intValue();
            userInfo = getById(id);    
            this.getJdbcTemplate().update("INSERT INTO authorities(username, authority) VALUES(?,?)", userInfo.getUsername(), role);
        } else {
            getJdbcTemplate().update("UPDATE users SET username=?, password=?,enabled=? WHERE iduser=?"
                    , userInfo.getUsername(), userInfo.getPassword(), userInfo.getEnabled(), userInfo.getIduser());
               this.getJdbcTemplate().update("UPDATE authorities set authority=? where username=?",  role,userInfo.getUsername());
            userInfo = getById(userInfo.getIduser());
        }
        
        return userInfo;
    }      

    public UserInfo getById(int id) {
        return this.getJdbcTemplate().queryForObject("SELECT * FROM users WHERE iduser=?", new UserInfoMapper(),id);
    }

    public void delete(Integer id) {
        UserInfo uInfo = getById(id);
        getJdbcTemplate().update("DELETE FROM authorities WHERE username =?",uInfo.getUsername());
        getJdbcTemplate().update("DELETE FROM users WHERE iduser =?",uInfo.getIduser());
    }
}
