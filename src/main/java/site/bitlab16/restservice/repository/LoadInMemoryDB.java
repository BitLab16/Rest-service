package site.bitlab16.restservice.repository;

import site.bitlab16.restservice.repository.TrackedPointRepository;
import site.bitlab16.restservice.model.TrackedPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadInMemoryDB {

    private static final Logger log = LoggerFactory.getLogger(LoadInMemoryDB.class);

    @Bean
    CommandLineRunner initDatabase(TrackedPointRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new TrackedPoint("Piazza dei signori", 150)));
            log.info("Preloading " + repository.save(new TrackedPoint("Prato della valle", 500)));
        };
    }
}
