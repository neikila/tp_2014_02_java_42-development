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
        try {
            Session session = sessionFactory.openSession();
            UserProfileDAO dao = new UserProfileDAO(session);
            Transaction transaction = session.beginTransaction();
            action.action(dao, param);
            transaction.commit();
        } catch (Exception e) {

        } finally {
            // todo transaction.commit
        }
    }
}
