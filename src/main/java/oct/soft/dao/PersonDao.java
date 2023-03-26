/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oct.soft.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import oct.soft.model.Office;
import oct.soft.model.OfficeMapper;
import oct.soft.model.Person;
import oct.soft.model.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 *
 * @author osantau
 */
@Repository
public class PersonDao {

    private final String SQL_GET_ALL = "select p.*,o.name as birou, b.name as filiala from person p \n"
            + "				left join offices_person op on op.idperson = p.idperson \n"
            + "				LEFT join office o on o.idoffice = op.idoffice\n"
            + "				LEFT join office b on b.idoffice = o.parent order by p.fname";
    private final String SQL_GET_BY_ID = "SELECT p.*,'' birou, '' filiala FROM person p WHERE p.idperson=?";
    private final String SQL_UPDATE = "UPDATE person set fname=?, lname=?, nickname=? WHERE idperson=?";
    private final String SQL_ADD_OFFICE = "INSERT INTO offices_person(idperson, idoffice) VALUES(?,?)";
    private final String SQL_REMOVE_OFFICE = "DELETE FROM  offices_person WHERE idperson=? AND idoffice=?";
    private final JdbcTemplate jdbcTemplate;
    private String where = "";

    @Autowired
    public PersonDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Person createOrUpdate(Person person) {       
        if (person.isNew()) {
            SimpleJdbcInsert sji = new SimpleJdbcInsert(jdbcTemplate).withTableName("person").usingGeneratedKeyColumns("idperson");
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("fname", person.getFname());
            parameters.put("lname", person.getFname());
            parameters.put("nickame", person.getNickname());
            parameters.put("telint_id", 0);
            int id = sji.executeAndReturnKey(parameters).intValue();
            person = getById(id);
        } else {
            jdbcTemplate.update(SQL_UPDATE, person.getFname(), person.getLname(), person.getNickname(), person.getIdperson());
            person = getById(person.getIdperson());
        }

        return person;
    }

    public Person getById(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_BY_ID, new PersonMapper(), id);
    }

    public List<Person> all() {
        List<Person> persons = new ArrayList<Person>();
        persons = jdbcTemplate.query(SQL_GET_ALL, new PersonMapper());
        return persons;
    }

    public Office findById(Integer id, boolean isBranch) {

        if (isBranch) {
            where = " AND parent=0";
        } else {
            where = " AND parent != 0";
        }
        return jdbcTemplate.queryForObject(SQL_GET_BY_ID + where, new OfficeMapper(), id);
    }

    public void deleteById(Integer id) {
        jdbcTemplate.execute("DELETE FROM person WHERE idperson=" + id);
    }

    public void addOfficeToPerson(int idperson, int idoffice) {
       jdbcTemplate.update(SQL_ADD_OFFICE, idperson,idoffice);
    }
   public void removeOffice(int idperson, int idoffice) {
       jdbcTemplate.update(SQL_REMOVE_OFFICE, idperson,idoffice);
    }
}
