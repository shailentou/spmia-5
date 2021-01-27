package com.example.demo.hystrix;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.hystrix.Hystrix;
import com.netflix.hystrix.config.HystrixCommandConfiguration.HystrixCommandExecutionConfig;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;

public class ThreadLocalConfiguration {

	@Autowired
	private HystrixConcurrencyStrategy exstingConcurrencyStrategy;
	
	@PostConstruct
	public void init() {
		HystrixEventNotifier eventNotifier=HystrixPlugins.getInstance().getEventNotifier();
		HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance().getMetricsPublisher();
		
		HystrixPropertiesStrategy hystrixPropertiesStrategy=HystrixPlugins.getInstance().getPropertiesStrategy();
		HystrixCommandExecutionHook hystrixCommandExecutionHook=HystrixPlugins.getInstance().getCommandExecutionHook();
		
		Hystrix.reset();
		HystrixPlugins.getInstance().registerConcurrencyStrategy(new ThreadLocalAwareStrategy( exstingConcurrencyStrategy));
		HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
		HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
				 
	}
}
