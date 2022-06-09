package lk.ijse.dep8.tasks.dao.custom.impl;

import lk.ijse.dep8.tasks.dao.custom.QueryDAO;
import org.hibernate.Session;

import java.sql.Connection;

public class QueryDAOImpl implements QueryDAO {

    private final Session session;

    public QueryDAOImpl(Session session) {
        this.session = session;
    }

}
