package dbService.dao;

import main.user.UserProfile;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class UserProfileDAO {
    private Session session;

    public UserProfileDAO(Session session) {
        this.session = session;
    }

    public void save(UserProfile dataSet) {
        session.save(dataSet);
        session.close();
    }

    public void update(UserProfile dataSet) {
        session.update(dataSet);
        session.flush();
        session.close();
    }

    public UserProfile read(long id) {
        return (UserProfile) session.load(UserProfile.class, id);
    }

    public UserProfile readByName(String name) {
        Criteria criteria = session.createCriteria(UserProfile.class);
        return (UserProfile) criteria.add(Restrictions.eq("login", name)).uniqueResult();
    }

    public void deleteAll() {
        session.createSQLQuery("delete from users").executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<UserProfile> readAll() {
        Criteria criteria = session.createCriteria(UserProfile.class);
        return (List<UserProfile>) criteria.list();
    }

    @SuppressWarnings("unchecked")
    public long countAll() {
        Criteria criteria = session.createCriteria(UserProfile.class);
        criteria.setProjection(Projections.rowCount());
        return (long)criteria.list().get(0);
    }

    @SuppressWarnings("unchecked")
    public List<UserProfile> readLimitOrder(int limit) {
        Criteria criteria = session.createCriteria(UserProfile.class);
        criteria.addOrder(Order.desc("score"));
        criteria.setMaxResults(limit);
        return (List<UserProfile>) criteria.list();
    }
}