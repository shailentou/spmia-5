package com.shail.organization.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/test")
public class TestController {

	
	
	@RequestMapping("/t1")
	public String initTest()
	{
		System.out.println("in controller test" );
		return "rest"+ "in controller test" ;
	}
}
