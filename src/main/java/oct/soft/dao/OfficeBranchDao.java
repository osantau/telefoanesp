/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oct.soft.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import oct.soft.model.Office;
import oct.soft.model.OfficeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 *
 * @author osantau
 */
@Repository
public class OfficeBranchDao {

    private final String SQL_GET_ALL = "SELECT * FROM office";
    private final String SQL_GET_BY_ID = "SELECT * FROM office WHERE idoffice=?";
    private final String SQL_UPDATE = "UPDATE office set name=?, isbranch=?, parent=? WHERE idoffice=?";
    private final JdbcTemplate jdbcTemplate;
    private String where = "";

    @Autowired
    public OfficeBranchDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void create(Office newOffice, boolean isBranch) {
        if (newOffice.isNew()) {
            SimpleJdbcInsert sji = new SimpleJdbcInsert(jdbcTemplate).withTableName("office");
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("name", newOffice.getName());
            if (isBranch) {
                parameters.put("isbranch", 1);
                parameters.put("parent", 0);
            } else {
                parameters.put("isbranch", 0);
                parameters.put("parent", newOffice.getParent());
            }
            sji.execute(parameters);
        } else {
            if (isBranch) {
                jdbcTemplate.update(SQL_UPDATE, newOffice.getName(), newOffice.getBranch(), 0, newOffice.getIdoffice());
            } else {
                jdbcTemplate.update(SQL_UPDATE, newOffice.getName(), 0, newOffice.getParent(), newOffice.getIdoffice());
            }
        }
    }

    public List<Office> all(boolean branch) {
        List<Office> offices = new LinkedList<Office>();
        if (branch) {
            where = " WHERE parent=0";
        } else {
            where = " WHERE parent!=0";
        }
        offices = jdbcTemplate.query(SQL_GET_ALL + where, new OfficeMapper());
        if (!branch) {
            for (Office o : offices) {
                o.setBranch(findById(o.getParent(), true));
            }
        }
        return offices;
    }

    public Office findById(Integer id, boolean isBranch) {

        if (isBranch) {
            where = " AND parent=0";
        } else {
            where = " AND parent != 0";
        }
        return jdbcTemplate.queryForObject(SQL_GET_BY_ID + where, new OfficeMapper(), id);
    }

    public List<Office> getOfficesByPerson(int idperson) {
        String sql = "select * from office where idoffice in (select idoffice from offices_person where idperson=?) ORDER BY name";
        List<Office> data = jdbcTemplate.query(sql, new OfficeMapper(), idperson);
        for (Office o : data) {
            o.setBranch(findById(o.getParent(), true));
        }
        return data;
    }

    public Map<Office, List<Office>> getBranchOfficeChilds(int idperson) {
        Map<Office, List<Office>> data = new LinkedHashMap<Office, List<Office>>();
        List<Office> branches = all(true);
        List<Office> personOffices = getOfficesByPerson(idperson);

        for (Office branch : branches) {
            List<Office> tempList = getOfficesByBranch(branch.getIdoffice());
            tempList.removeAll(personOffices);
            data.put(branch, tempList);
        }
        return data;
    }

    public List<Office> getOfficesByBranch(int branchId) {
        String sql = "SELECT * FROM office where isbranch=? and parent=? ORDER BY name";
        return jdbcTemplate.query(sql, new OfficeMapper(),0, branchId);
    }
     
}