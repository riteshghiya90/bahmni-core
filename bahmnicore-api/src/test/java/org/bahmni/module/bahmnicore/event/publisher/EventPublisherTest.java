package org.bahmni.module.bahmnicore.event.publisher;

import org.bahmni.module.bahmnicore.event.model.Event;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.openmrs.Person;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EventPublisherTest {

    private EventPublisher eventPublisher;

    private JmsTemplate jmsTemplate;


    @Before
    public void setUp() {
        jmsTemplate = mock(JmsTemplate.class);
        eventPublisher = new EventPublisher(jmsTemplate);
    }

    @Test
    public void shouldPublishEventOnTopicNameAsEventType() {

        Person person = getPerson();
        String eventType = "bahmni-patient";
        Event event = new Event(eventType, person, "bce786c0-aa57-480d-be6a-23692590086b");
        eventPublisher.onApplicationEvent(event);

        verify(jmsTemplate, times(1)).send(eq(eventType), any(MessageCreator.class));
    }

    private Person getPerson() {
        Person person = new Person();
        person.setId(123);
        person.setGender("M");
        person.setBirthdate(new Date(694224000000L));
        person.setUuid("bce786c0-aa57-480d-be6a-23692590086b");

        return person;
    }
}