package dbService.controller;

import dbService.dao.UserProfileDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Created by neikila on 02.05.15.
 */
public class TExecutor{

    private SessionFactory sessionFactory;

    public TExecutor (SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public <T, M> T actionUserProfileDAO(DBAction<T, M, UserProfileDAO> action, M param){
        Session session = sessionFactory.openSession();
        UserProfileDAO dao = new UserProfileDAO(session);
        T value = action.action(dao, param);
        return value;
    }

    public <M> void actionUserProfileDAOVoid(DBActionVoid<M, UserProfileDAO> action, M param){
        Session session = sessionFactory.openSession();
        UserProfileDAO dao = new UserProfileDAO(session);
        Transaction transaction = session.beginTransaction();
        action.action(dao, param);
        transaction.commit();
    }
}
