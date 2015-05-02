package dbService;

import Interface.DBService;
import dbService.dao.UserProfileDAO;
import main.user.UserProfile;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class DBServiceImpl implements DBService {
    private SessionFactory sessionFactory;

    public DBServiceImpl() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UserProfile.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/gameDB");
        configuration.setProperty("hibernate.connection.username", "gameAdmin");
        configuration.setProperty("hibernate.connection.password", "rfrltkf");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "validate");

        sessionFactory = createSessionFactory(configuration);
    }

    public String getLocalStatus() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String status = transaction.getLocalStatus().toString();
        session.close();
        return status;
    }

    public void vipe() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UserProfileDAO dao = new UserProfileDAO(session);
        dao.vipe();
        transaction.commit();
    }

    public void deleteAllUsers() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UserProfileDAO dao = new UserProfileDAO(session);
        dao.deleteAll();
        transaction.commit();
    }

    public void save(UserProfile dataSet) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UserProfileDAO dao = new UserProfileDAO(session);
        dao.save(dataSet);
        transaction.commit();
    }

    public UserProfile readUser(long id) {
        Session session = sessionFactory.openSession();
        UserProfileDAO dao = new UserProfileDAO(session);
        return dao.read(id);
    }

    public UserProfile readUserByName(String name) {
        Session session = sessionFactory.openSession();
        UserProfileDAO dao = new UserProfileDAO(session);
        return dao.readByName(name);
    }

    public List<UserProfile> readAll() {
        Session session = sessionFactory.openSession();
        UserProfileDAO dao = new UserProfileDAO(session);
        return dao.readAll();
    }

    public long countAllUsers() {
        Session session = sessionFactory.openSession();
        UserProfileDAO dao = new UserProfileDAO(session);
        return dao.countAll();
    }

    public List<UserProfile> readLimitOrder(int limit) {
        Session session = sessionFactory.openSession();
        UserProfileDAO dao = new UserProfileDAO(session);
        return dao.readLimitOrder(limit);
    }

    public void shutdown(){
        sessionFactory.close();
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}