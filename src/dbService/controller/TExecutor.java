package dbService.controller;

import dbService.dao.UserProfileDAO;
import org.hibernate.*;

/**
 * Created by neikila on 02.05.15.
 */
public class TExecutor{

    private SessionFactory sessionFactory;

    public TExecutor (SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public <T, M> T actionUserProfileDAO(DBAction<T, M, UserProfileDAO> action, M param){
        T value = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            UserProfileDAO dao = new UserProfileDAO(session);
            value = action.action(dao, param);
        } catch (HibernateException he) {
            he.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
        return value;
    }

    public <M> void actionUserProfileDAOVoid(DBActionVoid<M, UserProfileDAO> action, M param){
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            UserProfileDAO dao = new UserProfileDAO(session);
            transaction = session.beginTransaction();
            action.action(dao, param);
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null)
                transaction.rollback();
        } finally {
            if (transaction != null)
                transaction.commit();
            if (session != null && session.isOpen())
                session.close();
        }
    }
}
