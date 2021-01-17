package com.example.demo.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.client.OrganizationDiscoveryClient;
import com.example.demo.client.OrganizationFeignClient;
import com.example.demo.client.OrganizationRestTemplateClient;
import com.example.demo.model.License;
import com.example.demo.model.Organization;
import com.example.demo.repositery.LicenseRepository;

@Service
public class LicenseService {

	@Autowired
	private LicenseRepository licenseRepository;

	@Autowired
	OrganizationDiscoveryClient organizationDiscoveryClient;

	@Autowired
    OrganizationRestTemplateClient organizationRestClient;
	
	@Autowired
    OrganizationFeignClient organizationFeignClient;
	
	enum clientTypes {
		FEIGN, REST, DISCOVERY
	};

	public License getLicense(String organizationId, String licenseId, String clientType) {
		License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
		Organization org=retriveOrgInfo(organizationId,clientType);
		if(org!=null)
		license.setOrganizationName(org.getName());
		return license;
	}

	private Organization retriveOrgInfo(String organizationId, String clientType) {
		Organization org = null;
 
		switch (clientTypes.valueOf(clientType)) {

		case FEIGN:
			
			 System.out.println("I am using the feign client");
			 org = organizationFeignClient.getOrganization(organizationId);
			break;
		case REST:
			  System.out.println("I am using the rest client");
			  org = organizationRestClient.getOrganization(organizationId);
			break;
		case DISCOVERY:
			System.out.println("DFS");
			org = organizationDiscoveryClient.getOrganization(organizationId);
			break;
		}
		return org;
	}

	public List<License> getLicenseByOrg(String organizationId, String licenseId) {
		List<License> ls = licenseRepository.findByOrganizationId(organizationId);

		return ls;
	}

	public void saveLicense(License license) {
		license.withLicenseId(UUID.randomUUID().toString());
		licenseRepository.save(license);
	}

	public void updateLicense(License license) {
		licenseRepository.save(license);
	}

	public void deleteLicense(String licenseId) {
		License license = licenseRepository.findByLicenseId(licenseId);
		licenseRepository.delete(license);
	}
}
