package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceConfig{

  @Value("${spring.jpa.database}")
  private String exampleProperty="";

  public String getExampleProperty(){
    return exampleProperty;
  }
}