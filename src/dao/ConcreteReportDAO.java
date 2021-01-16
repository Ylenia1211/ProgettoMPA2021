package dao;

import datasource.ConnectionDBH2;
import model.Report;

import java.sql.ResultSet;

public class ConcreteReportDAO implements ReportDAO {
    private final ConnectionDBH2 connection_db;
    public  ConcreteReportDAO(ConnectionDBH2 connection_db) {
        this.connection_db = connection_db;
    }
    @Override
    public void add(Report item) {

    }

    @Override
    public ResultSet findAll() {
        return null;
    }

    @Override
    public void update(String id, Report item) {

    }

    @Override
    public void delete(String id) {

    }
}
