package site.bitlab16.restservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import site.bitlab16.restservice.model.Gathering;
import site.bitlab16.restservice.repository.GatheringRepository;

import java.util.List;

@Component
public class DbSinkService {

    private final GatheringRepository repository;

    @Autowired
    public DbSinkService(GatheringRepository repository) {
        this.repository = repository;
    }

    @Async
    @KafkaListener(topics = "${gathering-source-1.topic.name}", containerFactory = "gatheringKafkaListenerContainerFactory")
    public void listenTopicSource1(List<Gathering> gathering) {
        repository.saveAll(gathering);
    }

    @Async
    @KafkaListener(topics = "${gathering-source-2.topic.name}", containerFactory = "gatheringKafkaListenerContainerFactory")
    public void listenTopicSource2(List<Gathering> gathering) {
        repository.saveAll(gathering);
    }

    @Async
    @KafkaListener(topics = "${gathering-source-3.topic.name}", containerFactory = "gatheringKafkaListenerContainerFactory")
    public void listenTopicSource3(List<Gathering> gathering) {
        repository.saveAll(gathering);
    }

    @Async
    @KafkaListener(topics = "${gathering-source-4.topic.name}", containerFactory = "gatheringKafkaListenerContainerFactory")
    public void listenTopicSource4(List<Gathering> gathering) {
        repository.saveAll(gathering);
    }

    @Async
    @KafkaListener(topics = "${gathering-source-5.topic.name}", containerFactory = "gatheringKafkaListenerContainerFactory")
    public void listenTopicSource5(List<Gathering> gathering) {
        repository.saveAll(gathering);
    }
}
