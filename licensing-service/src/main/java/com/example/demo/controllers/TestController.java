package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/test")
public class TestController {

	//@Value("${example.property}")
//	String configprop;
	
	@RequestMapping("/t1")
	public String initTest()
	{
		System.out.println("in controller test" );
		return "rest"+ "in controller test" ;
	}
}
