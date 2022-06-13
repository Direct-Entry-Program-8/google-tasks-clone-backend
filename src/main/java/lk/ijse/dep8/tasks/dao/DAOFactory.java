package lk.ijse.dep8.tasks.dao;

import lk.ijse.dep8.tasks.dao.custom.impl.QueryDAOImpl;
import lk.ijse.dep8.tasks.dao.custom.impl.TaskDAOImpl;
import lk.ijse.dep8.tasks.dao.custom.impl.TaskListDAOImpl;
import lk.ijse.dep8.tasks.dao.custom.impl.UserDAOImpl;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.sql.Connection;

public class DAOFactory {

    private static DAOFactory daoFactory;

    private DAOFactory(){

    }

    public static DAOFactory getInstance(){
        return (daoFactory == null)? (daoFactory = new DAOFactory()): daoFactory;
    }

    public <T extends SuperDAO> T getDAO(EntityManager em, DAOTypes daoType){
        switch (daoType){
            case USER:
                return (T) new UserDAOImpl(em);
            case TASK_LIST:
                return (T) new TaskListDAOImpl(em);
            case TASK:
                return (T) new TaskDAOImpl(em);
            case QUERY:
                return (T) new QueryDAOImpl(em);
            default:
                return null;
        }
    }

    public enum DAOTypes{
        USER, TASK_LIST, TASK, QUERY
    }
}
