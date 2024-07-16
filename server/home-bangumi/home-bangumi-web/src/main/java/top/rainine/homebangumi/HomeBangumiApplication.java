package top.rainine.homebangumi;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class HomeBangumiApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(HomeBangumiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        initServices();
    }

    private void initServices() {

    }
}
