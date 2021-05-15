package repository.jdbc.hbm;

import domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.UserRepository;
import repository.jdbc.JdbcUtils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class UserJdbcHbmRepository implements UserRepository {

    private final JdbcUtils jdbcUtils;
    private static final Logger logger= LogManager.getLogger();
    static SessionFactory sessionFactory;

    public UserJdbcHbmRepository(Properties properties) {

        jdbcUtils = new JdbcUtils(properties);
        initialize();
    }

    static void initialize() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    static void close() {
        if (sessionFactory != null) sessionFactory.close();
    }

    @Override
    public User findOne(Integer id) {
        List<User> result = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                result = session.createQuery("FROM User WHERE id = :ID", User.class)
                        .setInteger("ID", id).list();
                transaction.commit();
            }
            catch (RuntimeException exception) {
                if (transaction != null) transaction.rollback();
            }
        }
        return result.get(0);
    }

    @Override
    public User findUsernamePassword(String username, String password) {
        logger.traceEntry("Finding task with username and password {}, {}", username, password);
        Connection connection = jdbcUtils.getConnection();

        List<User> result = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                result = session.createQuery
                        (" FROM User WHERE username = :USERNAME AND password :=PASSWORD", User.class)
                        .setParameter("USERNAME", username)
                        .setParameter("PASSWORD", password).list();
                transaction.commit();
            }
            catch (RuntimeException exception) {
                if (transaction != null) transaction.rollback();
            }
        }
        return result.get(0);
    }

    @Override
    public Iterable<User> findAll() {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();

        List<User> users = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                users = session.createQuery("FROM User", User.class).list();
                transaction.commit();
            }
            catch (RuntimeException exception) {
                if (transaction != null) transaction.rollback();
            }
        }
        return users;
    }

    @Override
    public void save(User entity) {
        logger.traceEntry("Saving task {}", entity);
        Connection connection = jdbcUtils.getConnection();

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(entity);
                transaction.commit();
            }
            catch (RuntimeException exception) {
                if (transaction != null) transaction.rollback();
            }
        }
    }

    @Override
    public void delete(Integer id){
        logger.traceEntry("Deleting task with {}", id);
        Connection connection = jdbcUtils.getConnection();

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.delete(id);
                transaction.commit();
            }
            catch (RuntimeException exception) {
                if (transaction != null) transaction.rollback();
            }
        }
    }

    @Override
    public void update(User entity){
        logger.traceEntry("Updating task {}", entity);
        Connection connection = jdbcUtils.getConnection();

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.update(entity);
                transaction.commit();
            }
            catch (RuntimeException exception) {
                if (transaction != null) transaction.rollback();
            }
        }
    }

}
