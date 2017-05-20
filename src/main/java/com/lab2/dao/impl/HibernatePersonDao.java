package com.lab2.dao.impl;

import com.lab2.dao.PersonDao;
import com.lab2.domain.Organization;
import com.lab2.domain.Person;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class HibernatePersonDao implements PersonDao {

    private SessionFactory sessionFactory;

    public HibernatePersonDao() {
        Configuration configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
    }

    @Override
    public void store(Person person) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            if (person.getOrganization() != null) {
                Organization org = (Organization) session.get(Organization.class, person.getOrganization().getId());
                if (org != null) {
                    person.setOrganization(org);
                } else {
                    throw new RuntimeException();
                }
            }
            session.save(person);
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public Person update(Person personChanges) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Person person = (Person) session.get(Person.class, personChanges.getId());
            person.setEmail(personChanges.getEmail());
            if (personChanges.getFirstname() != null) {
                person.setFirstname(personChanges.getFirstname());
            }
            if (personChanges.getLastname() != null) {
                person.setLastname(personChanges.getLastname());
            }
            if (personChanges.getDescription() != null) {
                person.setDescription(personChanges.getDescription());
            }
            if (personChanges.getAddress() != null) {
                person.setAddress(personChanges.getAddress());
            }
            if (personChanges.getOrganization() != null) {
                Organization org = (Organization) session.get(Organization.class, personChanges.getOrganization().getId());
                if (org != null) {
                    person.setOrganization(org);
                } else {
                    throw new RuntimeException();
                }
            }
            session.update(person);
            tx.commit();
            return person;
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(long personId) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Person person = (Person) session.get(Person.class, personId);
            List<Person> friends = person.getFriends();
            for (Person friend : friends) {
                friends.remove(friend);
                friend.getFriends().remove(person);
                session.update(friend);
            }
            session.update(person);
            session.delete(person);
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Person findById(long personId) {
        Session session = sessionFactory.openSession();
        try {
            Person person = (Person) session.get(Person.class, personId);
            if (person != null) {
                return person;
            } else {
                throw new RuntimeException();
            }
        } finally {
            session.close();
        }
    }

    @Override
    public List<Person> findAll() {
        Session session = sessionFactory.openSession();
        try {
            Query query = session.createQuery("from Person");
            return query.list();
        } finally {
            session.close();
        }
    }
}
