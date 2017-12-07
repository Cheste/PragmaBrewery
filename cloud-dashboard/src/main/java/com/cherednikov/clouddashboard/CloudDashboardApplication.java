package com.cherednikov.clouddashboard;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAdminServer
public class CloudDashboardApplication {

//	public static void main(String[] args) {
//		SpringApplication.run(CloudDashboardApplication.class, args);
//	}

	public static void main(String[] args) {
		String str = "absdfghtrewwer";

		Map<Integer, Long> collect = str.chars().boxed().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		for (Map.Entry<Integer, Long> integerLongEntry : collect.entrySet()) {
			if (integerLongEntry.getValue() > 1) {
				System.out.println((char)integerLongEntry.getKey().intValue());
			}
		}
	}
}
