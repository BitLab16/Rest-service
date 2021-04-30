package site.bitlab16.restservice.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import site.bitlab16.restservice.model.Gathering;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class PredictionRepository {

    private static final Logger LOGGER = Logger.getLogger( PredictionRepository.class.getName() );

    private final RestTemplate restTemplate;

    private static final  String PREDICTION_JSON_NAME = "predictions";
    private static final  String POINT_ID_JSON_NAME = "point_id";
    private static final  String DETECTION_TIME_JSON_NAME = "time";
    private static final  String FLOW_JSON_NAME = "flow";

    @Value(value = "${ml.apiAddress}")
    private String baseUrl;

    PredictionRepository() {
        restTemplate = new RestTemplate();
    }


    public List<Gathering> findAllFromInterval(LocalDateTime from, LocalDateTime to)
    {
        var apiResult = getResultStringFromInterval(from, to);
        var mapper = new ObjectMapper();
        try {
            var jsonNode = mapper.readTree(apiResult);
            var gatherings = new LinkedList<Gathering>();
            for (var gathering_json: jsonNode) {
                var pointId = gathering_json.get(POINT_ID_JSON_NAME).asLong();
                var arrayNode = gathering_json.get(PREDICTION_JSON_NAME);
                for (var node : arrayNode) {
                    gatherings.add(getGatheringFromJson(node, pointId));
                }
            }
            return gatherings;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
        }

        return Collections.emptyList();
    }

    public List<Gathering> findByIdFromInterval(Long pointId, LocalDateTime from, LocalDateTime to)
    {
        var apiResult = getResultStringByIdFromInterval(pointId, from, to);
        var mapper = new ObjectMapper();
        try {
            var jsonNode = mapper.readTree(apiResult);
            var gatherings = new LinkedList<Gathering>();
            var arrayNode = jsonNode.get(PREDICTION_JSON_NAME);
            for (var node : arrayNode) {
                gatherings.add(getGatheringFromJson(node, pointId));
            }
            return gatherings;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
        }
        return Collections.emptyList();
    }

    public List<Gathering> findOnlyHoursByIdFromInterval(Long pointId, LocalDateTime from, LocalDateTime to) {

        var apiResult = getResultStringByIdFromInterval(pointId, from, to);
        var mapper = new ObjectMapper();
        try {
            var jsonNode = mapper.readTree(apiResult);
            var gatherings = new LinkedList<Gathering>();
            var arrayNode = jsonNode.get(PREDICTION_JSON_NAME);
            for (var node : arrayNode) {
                var timeAsString =  node.get(DETECTION_TIME_JSON_NAME).asText();
                var time = Timestamp.valueOf(timeAsString);
                if(isHourOrHalfAnHourTime(time)) {
                    gatherings.add(getGatheringFromJson(node, pointId));
                }
            }
            return gatherings;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
        }
        return Collections.emptyList();
    }

    public List<Gathering> findAllOnlyHoursFromInterval(LocalDateTime from, LocalDateTime to)
    {
        var apiResult = getResultStringFromInterval(from, to);
        var mapper = new ObjectMapper();
        try {
            var jsonNode = mapper.readTree(apiResult);
            var gatherings = new LinkedList<Gathering>();
            for (var gathering_json: jsonNode) {
                var pointId = gathering_json.get(POINT_ID_JSON_NAME).asLong();
                var arrayNode = gathering_json.get(PREDICTION_JSON_NAME);
                for (var node : arrayNode) {
                    var timeAsString =  node.get(DETECTION_TIME_JSON_NAME).asText();
                    var time = Timestamp.valueOf(timeAsString);
                    if(isHourOrHalfAnHourTime(time)) {
                        gatherings.add(getGatheringFromJson(node, pointId));
                    }
                }
            }
            return gatherings;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
        }
        return Collections.emptyList();
    }

    private boolean isHourOrHalfAnHourTime(Timestamp time) {
        return time.toLocalDateTime().getMinute() == 0 || time.toLocalDateTime().getMinute() == 30;
    }

    private  String getResultStringFromInterval(LocalDateTime from, LocalDateTime to) {
        var uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/prediction/")
                .queryParam("from", from.toString().replace('T', ' ') )
                .queryParam("to", to.toString().replace('T', ' ') );
        var uri = uriBuilder.build().toUri();

        return  restTemplate.getForObject(uri,  String.class);
    }

    private  String getResultStringByIdFromInterval(Long pointId, LocalDateTime from, LocalDateTime to) {
        var uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/prediction/"+pointId+"/")
                .queryParam("from", from.toString().replace('T', ' ') )
                .queryParam("to", to.toString().replace('T', ' ') );
        var uri = uriBuilder.build().toUri();

        return  restTemplate.getForObject(uri,  String.class);
    }

    private Gathering getGatheringFromJson(JsonNode node, Long pointId) {
        var gathering = new Gathering();
        gathering.setPoint(pointId);
        var timeAsString = node.get(DETECTION_TIME_JSON_NAME).asText();
        gathering.setDetectionTime(Timestamp.valueOf(timeAsString));
        var flow = node.get(FLOW_JSON_NAME).asInt();
        gathering.setFlow(flow);
        return gathering;
    }

}
