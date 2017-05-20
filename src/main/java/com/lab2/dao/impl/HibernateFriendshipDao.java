package com.lab2.dao.impl;

import com.lab2.dao.FriendshipDao;
import com.lab2.domain.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class HibernateFriendshipDao implements FriendshipDao {

    private SessionFactory sessionFactory;

    public HibernateFriendshipDao() {
        Configuration configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
    }

    @Override
    public void create(long userId1, long userId2) {

        Session session = sessionFactory.openSession();
        Transaction tx = session.getTransaction();

        try {
            tx.begin();
            Person person1 = (Person) session.get(Person.class, userId1);
            Person person2 = (Person) session.get(Person.class, userId2);

            List<Person> friends = person1.getFriends();
            if (!friends.contains(person2)) {
                friends.add(person2);
                session.update(person1);
            }

            friends = person2.getFriends();
            if (!friends.contains(person1)) {
                friends.add(person1);
                session.update(person2);
            }
            tx.commit();
        }catch(RuntimeException e){
            tx.rollback();
            throw e;
        }finally {
            session.close();
        }
    }

    @Override
    public void delete(long userId1, long userId2) {

        Session session = sessionFactory.openSession();
        Transaction tx = session.getTransaction();

        try {
            tx.begin();
            Person person1 = (Person) session.get(Person.class, userId1);
            Person person2 = (Person) session.get(Person.class, userId2);

            List<Person> friends = person1.getFriends();
            if (friends.contains(person2)) {
                friends.remove(person2);
                session.update(person1);
            } else {
                throw new RuntimeException();
            }

            friends = person2.getFriends();
            if (friends.contains(person1)) {
                friends.remove(person1);
                session.update(person2);
            } else {
                throw new RuntimeException();
            }
            tx.commit();
        }catch(RuntimeException e){
            tx.rollback();
            throw e;
        }finally {
            session.close();
        }
    }
}
