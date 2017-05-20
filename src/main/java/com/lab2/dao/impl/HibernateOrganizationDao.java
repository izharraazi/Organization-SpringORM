package com.lab2.dao.impl;

import com.lab2.dao.OrganizationDao;
import com.lab2.domain.Organization;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class HibernateOrganizationDao implements OrganizationDao {

    private SessionFactory sessionFactory;

    public HibernateOrganizationDao() {
        Configuration configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
    }

    @Override
    public void add(Organization organization) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.getTransaction();
        try{
            tx.begin();
            session.save(organization);
            tx.commit();
        }catch(RuntimeException e){
            tx.rollback();
            throw e;
        }finally {
            session.close();
        }
    }

    public Organization update(Organization orgChanges) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.getTransaction();
        try{
            tx.begin();
            Organization org = (Organization) session.get(Organization.class, orgChanges.getId());
            org.setName(orgChanges.getName());
            if (orgChanges.getDescription() != null) {
                org.setDescription(orgChanges.getDescription());
            }
            if (orgChanges.getAddress() != null) {
                org.setAddress(orgChanges.getAddress());
            }
            session.update(org);
            tx.commit();
            return org;
        }catch(RuntimeException e){
            tx.rollback();
            throw e;
        }finally {
            session.close();
        }
    }

    @Override
    public void delete(long orgId) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.getTransaction();
        try{
            tx.begin();
            Organization org = (Organization) session.get(Organization.class, orgId);
            if (org.getPersons().isEmpty()) {
                session.delete(org);
            } else {
                throw new RuntimeException("ORG_NOT_EMPTY");
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
    public Organization findById(long orgId) {
        Session session = sessionFactory.openSession();
        try{
            Organization org = (Organization) session.get(Organization.class, orgId);
            if (org != null) {
                return org;
            } else {
                throw new RuntimeException();
            }
        }finally {
            session.close();
        }
    }

    @Override
    public List<Organization> findAll() {
        Session session = sessionFactory.openSession();
        try{
            Query query = session.createQuery("from Organization");
            return query.list();
        }finally {
            session.close();
        }
    }
}
