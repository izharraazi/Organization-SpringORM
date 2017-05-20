package com.lab2.dao;

import com.lab2.domain.Organization;

import java.util.List;

public interface OrganizationDao {
    void add(Organization organization);

    void delete(long orgId);

    Organization findById(long orgId);

    Organization update(Organization org);

    List<Organization> findAll();
}
