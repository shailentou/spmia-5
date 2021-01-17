package com.example.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.ServiceConfig;
import com.example.demo.model.License;
import com.example.demo.model.Organization;
import com.example.demo.services.LicenseService;

@RestController
@RequestMapping(value = "/v1/{organizationId}/licenses")
public class LicenseServiceController {

	@Autowired
	private LicenseService licenseService;  

	@Autowired
    private ServiceConfig serviceConfig;
	
	@RequestMapping(value = "/{licenseId}/{clientType}", method = RequestMethod.GET)
	public License getLicensesWithClient(@PathVariable("organizationId") String organizationId,
			@PathVariable("licenseId") String licenseId,@PathVariable("clientType") String clientType) {
		
		
		License license=	licenseService.getLicense(organizationId, licenseId,clientType);
		return license;
		//return new License().withLicenseId(licenseId).withLicenseType("Seat").withOrganizationId(organizationId)
			//	.withProductName("Teleco");
	}
	
	/*
	@RequestMapping(value = "/{licenseId}", method = RequestMethod.GET)
	public License getLicenses(@PathVariable("organizationId") String organizationId,
			@PathVariable("licenseId") String licenseId) {
		
		
		
		licenseService.getLicense(organizationId, licenseId);
		return new License().withLicenseId(licenseId).withLicenseType("Seat").withOrganizationId(organizationId)
				.withProductName("Teleco");
	}*/

	@RequestMapping(value = "{licenseId}", method = RequestMethod.PUT)
	public String updateLicenses(@RequestBody License license) {
		licenseService.updateLicense(license);
		return String.format("This is the put");
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String saveLicenses(@RequestBody License license) {
		licenseService.saveLicense(license);
		return String.format("This is the post 1");
	}

	@RequestMapping(value = "{licenseId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public String deleteLicenses(@PathVariable("licenseId") String licenseId) {
		licenseService.deleteLicense(licenseId);
		return String.format("This is the Delete");
	}
}
