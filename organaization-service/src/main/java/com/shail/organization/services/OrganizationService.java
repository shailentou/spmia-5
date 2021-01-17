package com.shail.organization.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shail.organization.model.Organization;
import com.shail.organization.repository.OrganizationRepository;

@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository orgRepository;
/*
    public Organization getOrg(String organizationId) {
        return orgRepository.findById(organizationId).get();
    }
    */
    
    public Optional<Organization> getOrg(String organizationId) {
        return Optional.of(orgRepository.findById(organizationId).get());
    }

    public void saveOrg(Organization org){
      //  org.setOrganizationId( UUID.randomUUID().toString());

        orgRepository.save(org);

    }

    public void updateOrg(Organization org){
        orgRepository.save(org);
    }

    public void deleteOrg(Organization org){
        orgRepository.deleteById( org.getOrganizationId());
    }
}
