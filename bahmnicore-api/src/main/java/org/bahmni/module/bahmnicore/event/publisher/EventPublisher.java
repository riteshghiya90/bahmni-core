package org.bahmni.module.bahmnicore.event.publisher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bahmni.module.bahmnicore.event.model.Event;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.event.EventListener;
import org.springframework.jms.core.JmsTemplate;

import java.io.IOException;

public class EventPublisher {

    private static final Logger log = LogManager.getLogger(EventPublisher.class);

    private final JmsTemplate jmsTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public EventPublisher(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @EventListener
    public void onApplicationEvent(Event event) {
        String jsonPayload = toJsonPayload(event.payload);
        jmsTemplate.send(event.eventType, session -> session.createTextMessage(jsonPayload));
        log.debug("Published Message" + event.payload);
    }

    private String toJsonPayload(Object event) {
        String payload;
        try {
            payload = objectMapper.writeValueAsString(event);
        }
        catch (IOException exception) {
            log.error("Error while creating event payload : ", exception);
            throw new RuntimeException(exception);
        }
        return payload;
    }
}
