package com.example.demo.util;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class LicenseUtil {

	
	public void randomRunLong() {
		Random rndm= new Random();
		
		int randomnum= rndm.nextInt( (3-1)+1)+1;
		
		if(randomnum==3)sleep();
	}
	
	private void sleep() {
		try {
			Thread.sleep(10000);
		}
		catch( InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
