package com.example.demo.services;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.client.OrganizationDiscoveryClient;
import com.example.demo.client.OrganizationFeignClient;
import com.example.demo.client.OrganizationRestTemplateClient;
import com.example.demo.model.License;
import com.example.demo.model.Organization;
import com.example.demo.repositery.LicenseRepository;
import com.example.demo.util.LicenseUtil;
import com.example.demo.util.UserContextFilter;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

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

	private License buildFallbackLicenseList(String organizationId, String licenseId, String clientType) {
		return new License().withLicenseId("100").withLicenseType("test");
	}

	@Autowired
	LicenseUtil licenseUtil;

	/*
	 * @HystrixCommand(fallbackMethod = "buildFallbackLicenseList",
	 * commandProperties = {
	 * 
	 * @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
	 * value = "22000")
	 * 
	 * }) public License getLicense(String organizationId, String licenseId, String
	 * clientType) throws RuntimeException { try {
	 * System.out.println(Instant.now()); Thread.sleep(23000);
	 * 
	 * System.out.println(Instant.now()); } catch (InterruptedException e) {
	 * e.printStackTrace(); }
	 * 
	 * licenseUtil.randomRunLong(); License license =
	 * licenseRepository.findByOrganizationIdAndLicenseId(organizationId,
	 * licenseId); Organization org = retriveOrgInfo(organizationId, clientType); if
	 * (org != null) license.setOrganizationName(org.getName()); return license; }
	 */
	private static final Logger log = LoggerFactory.getLogger(LicenseService.class);

	@HystrixCommand(fallbackMethod = "buildFallbackLicenseList", threadPoolKey = "licenseIdThreadPool", threadPoolProperties = {
			@HystrixProperty(name = "coreSize", value = "30"), @HystrixProperty(name = "maxQueueSize", value = "10") },

			commandProperties = {

					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
					@HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "15000"),
					@HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5")

			})
	public License getLicense(String organizationId, String licenseId, String clientType) throws RuntimeException {
	
		log.info("Inside License Service");
		License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
		/*
		 * 
		  Organization org = retriveOrgInfo(organizationId, clientType); if (org !=
		  null) license.setOrganizationName(org.getName()); return license;
		 */
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

	@HystrixCommand(fallbackMethod = "buildFallbackLicenseList", threadPoolKey = "licenseByOrgThreadPool", threadPoolProperties = {
			@HystrixProperty(name = "coreSize", value = "30"), @HystrixProperty(name = "maxQueueSize", value = "10") },

			commandProperties = {

					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
					@HystrixProperty(name = "metrics.rollingStates.timeInmilliseconds", value = "15000"),
					@HystrixProperty(name = "metrics.rollingStates.numBuckets", value = "5")

			})
	public List<License> getLicenseByOrg(String organizationId, String licenseId) {
		licenseUtil.randomRunLong();
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
