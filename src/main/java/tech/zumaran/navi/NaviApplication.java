package tech.zumaran.navi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class NaviApplication {

	public static void main(String[] args) {
		SpringApplication.run(NaviApplication.class, args);
	}

}
