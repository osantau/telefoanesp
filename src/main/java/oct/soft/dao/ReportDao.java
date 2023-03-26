/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oct.soft.dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import oct.soft.dao.beans.BirouBean;
import oct.soft.dao.beans.PersonBean;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author osantau
 */
@Repository
public class ReportDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReportDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<BirouBean> searchBranch(String keyword) {
        String sql = "SELECT o.idoffice\n"
                + "                        ,o.name \n"
                + "                        ,p.number\n"
                + "                  FROM office o \n"
                + "                  INNER JOIN phone p on(o.idoffice=p.idoffice) \n"
                + "                  WHERE lower(o.name) like '%" + keyword.toLowerCase() + "%' AND o.parent=0\n"
                + "                  ORDER BY o.name ASC";
        return jdbcTemplate.query(sql, new ResultSetExtractor<List<BirouBean>>() {

            @Override
            public List<BirouBean> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<BirouBean> list = new ArrayList<BirouBean>();
                while (rs.next()) {
                    BirouBean bb = new BirouBean();
                    bb.setIdoffice(rs.getInt("idoffice"));
                    bb.setName(rs.getString("name"));
                    bb.setNumber(rs.getString("number"));
                    list.add(bb);
                }
                return list;
            }

        });
    }

    public List<BirouBean> searchOffice(String keyword) {
        String sql = "SELECT o.idoffice\n"
                + "                        ,o.name \n"
                + "                        ,p.number\n"
                + "                        ,o1.name as branch\n"
                + "                  FROM office o \n"
                + "                  INNER JOIN phone p on(o.idoffice=p.idoffice) \n"
                + "                  INNER JOIN office o1 on(o.parent=o1.idoffice) \n"
                + "                  WHERE lower(o.name) like '%" + keyword.toLowerCase() + "%'\n"
                + "                  ORDER BY o.name ASC";
        return jdbcTemplate.query(sql, new ResultSetExtractor<List<BirouBean>>() {

            @Override
            public List<BirouBean> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<BirouBean> list = new ArrayList<BirouBean>();
                while (rs.next()) {
                    BirouBean bb = new BirouBean();
                    bb.setIdoffice(rs.getInt("idoffice"));
                    bb.setName(rs.getString("name"));
                    bb.setNumber(rs.getString("number"));
                    bb.setBranch(rs.getString("branch"));
                    list.add(bb);
                }
                return list;
            }

        });

    }

    public List<PersonBean> searchPerson(String keyword) {
        keyword = keyword.toLowerCase();
        String sql = "SELECT fname,lname,name,branch,group_concat(number) as number,idperson,telserv,telfix,telmobil,idoffice\n"
                + "                    FROM search WHERE fname LIKE '%" + keyword + "%' OR lname LIKE '%" + keyword + "%' \n"
                + "                    OR CONCAT(fname,' ',lname) LIKE '%" + keyword + "%' \n"
                + "                    OR CONCAT(lname,' ',fname) LIKE '%" + keyword + "%' \n"
                + "                    OR nickname LIKE '%" + keyword + "%' \n"
                + "                    OR CONCAT(fname,' ',nickname) LIKE '%" + keyword + "%' \n"
                + "                    OR CONCAT(lname,' ',nickname) LIKE '%" + keyword + "%'\n"
                + "                    OR CONCAT(nickname,' ',fname) LIKE '%" + keyword + "%' \n"
                + "                    OR CONCAT(nickname,' ',lname) LIKE '%" + keyword + "%' \n"
                + "                    OR number LIKE '%" + keyword + "%' \n"
                + "                    OR telserv LIKE '%" + keyword + "%' GROUP BY fname,lname,name,idperson,telserv,telfix,telmobil,idoffice ORDER BY fname";

        return jdbcTemplate.query(sql, new ResultSetExtractor<List<PersonBean>>() {

            @Override
            public List<PersonBean> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<PersonBean> list = new ArrayList<PersonBean>();
                while (rs.next()) {
                    PersonBean pb = new PersonBean();
                    pb.setFname(rs.getString("fname"));
                    pb.setLname(rs.getString("lname"));
                    pb.setName(rs.getString("name"));
                    pb.setBranch(rs.getString("branch"));
                    pb.setNumber(rs.getString("number"));
                    pb.setIdperson(rs.getInt("idperson"));
                    pb.setTelserv(rs.getString("telserv"));
                    pb.setTelfix(rs.getString("telfix"));
                    pb.setTelmobil(rs.getString("telmobil"));
                    pb.setIdoffice(rs.getInt("idoffice"));
                    list.add(pb);
                }
                return list;
            }
        });
    }

    public int maxNumePrenume(List<PersonBean> list) {
        List<Integer> numPrenList = new ArrayList<Integer>();
        for (PersonBean pb : list) {
            numPrenList.add(pb.numePrenumeLength());
        }
        return (Integer) Collections.max(numPrenList);
    }

    public boolean hasPersonNumber(Integer idperson, String number, Integer idoffice) {
        String checkSql = "select count(idperson) cnt from person where idperson=? and telint_id = (select idphone from phone where number=? and idoffice=?) ";
        Integer result = (Integer) jdbcTemplate.queryForObject(checkSql, Integer.class, idperson, number, idoffice);
        return result >= 1;
    }

    public List<BirouBean> interioareFiliale() {
        String sql = "SELECT o.name as birou, group_concat(t.number ORDER BY t.number DESC SEPARATOR ', ') as numere "
                + " from office o inner join phone t on(o.idoffice=t.idoffice) WHERE o.isbranch=1 group by o.name";

        return jdbcTemplate.query(sql, new ResultSetExtractor<List<BirouBean>>() {

            @Override
            public List<BirouBean> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<BirouBean> list = new ArrayList<BirouBean>();
                while (rs.next()) {
                    BirouBean bb = new BirouBean();
                    bb.setBirou(rs.getString("birou"));
                    bb.setNumere(rs.getString("numere"));
                    list.add(bb);
                }
                return list;
            }
        });

    }

    public List<BirouBean> interioareBirouri() {
        String sql = "SELECT o.name as birou, group_concat(p.number ORDER BY p.number ASC SEPARATOR ', ') as numere FROM office o \n"
                + "                    JOIN phone p on (o.idoffice=p.idoffice and p.interior=1) where o.isbranch=0 group by name";
        return jdbcTemplate.query(sql, new ResultSetExtractor<List<BirouBean>>() {

            @Override
            public List<BirouBean> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<BirouBean> list = new ArrayList<BirouBean>();
                while (rs.next()) {
                    BirouBean bb = new BirouBean();
                    bb.setBirou(rs.getString("birou"));
                    bb.setNumere(rs.getString("numere"));
                    list.add(bb);
                }
                return list;
            }
        });

    }

    public List<PersonBean> interioarePersonal() {
        String sql = "select person.fname nume,person.lname prenume, group_concat(phone.number ORDER BY phone.number ASC SEPARATOR ', ') as numere\n"
                + "                    ,(SELECT number from phone where idphone = person.telint_id) as  numar_asociat\n"
                + "                    from person \n"
                + "                    join offices_person on (person.idperson = offices_person.idperson)\n"
                + "                    join office on(office.idoffice = offices_person.idoffice)\n"
                + "                    join phone on(phone.idoffice = office.idoffice and phone.interior=1) \n"
                + "                    group by fname,lname,telint_id";
        return jdbcTemplate.query(sql, new ResultSetExtractor<List<PersonBean>>() {

            @Override
            public List<PersonBean> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<PersonBean> list = new ArrayList<PersonBean>();
                while (rs.next()) {
                    PersonBean pb = new PersonBean();
                    pb.setNume(rs.getString("nume"));
                    pb.setPrenume(rs.getString("prenume"));
                    pb.setNumere(rs.getString("numere"));
                    pb.setNumar_asociat(rs.getString("numar_asociat"));
                    list.add(pb);
                }
                return list;
            }
        });
    }

    public ByteArrayOutputStream reportFiliale() {
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet spreadsheet = wb.createSheet();
        XSSFRow row = spreadsheet.createRow(0);
        row.createCell(0).setCellValue("Filiala");
        row.createCell(1).setCellValue("Numere");

        int idx = 1;
        for (BirouBean bb : interioareFiliale()) {
            row = spreadsheet.createRow(idx);
            row.createCell(0).setCellValue(bb.getBirou());
            row.createCell(1).setCellValue(bb.getNumere());
            idx++;
        }
        spreadsheet.autoSizeColumn(0);
        spreadsheet.autoSizeColumn(1);

        try {
            wb.write(outByteStream);
            outByteStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return outByteStream;
    }

    public ByteArrayOutputStream reportBirouri() {
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet spreadsheet = wb.createSheet();
        XSSFRow row = spreadsheet.createRow(0);
        row.createCell(0).setCellValue("Birou");
        row.createCell(1).setCellValue("Numere");

        int idx = 1;
        for (BirouBean bb : interioareBirouri()) {
            row = spreadsheet.createRow(idx);
            row.createCell(0).setCellValue(bb.getBirou());
            row.createCell(1).setCellValue(bb.getNumere());
            idx++;
        }
        spreadsheet.autoSizeColumn(0);
        spreadsheet.autoSizeColumn(1);

        try {
            wb.write(outByteStream);
            outByteStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return outByteStream;
    }

    public ByteArrayOutputStream reportPersonal() {
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet spreadsheet = wb.createSheet();
        XSSFRow row = spreadsheet.createRow(0);
        row.createCell(0).setCellValue("Nume");
        row.createCell(1).setCellValue("Prenume");
        row.createCell(2).setCellValue("Numere");
        row.createCell(3).setCellValue("Numar asociat");

        int idx = 1;
        for (PersonBean pb : interioarePersonal()) {
            row = spreadsheet.createRow(idx);
            row.createCell(0).setCellValue(pb.getNume());
            row.createCell(1).setCellValue(pb.getPrenume());
            row.createCell(2).setCellValue(pb.getNumere());
            row.createCell(3).setCellValue(pb.getNumar_asociat());
            idx++;
        }
        spreadsheet.autoSizeColumn(0);
        spreadsheet.autoSizeColumn(1);
        spreadsheet.autoSizeColumn(2);
        spreadsheet.autoSizeColumn(3);

        try {
            wb.write(outByteStream);
            outByteStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return outByteStream;
    }
    public void removePersonInt(String number, int idperson)
        {
        	String sql = "UPDATE person set telint_id=0 where idperson=?";
        	jdbcTemplate.update(sql, idperson);
        }

    public void addPersonInt(String numar, Integer idperson, Integer idoffice) {
      String sql = "UPDATE person set telint_id=(select idphone from phone where number=? and idoffice= ?) where idperson=?";
      jdbcTemplate.update(sql,numar,idoffice,idperson);
    }
}
