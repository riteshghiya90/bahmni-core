package org.bahmni.module.bahmnicore.myadvice;

import org.openmrs.Patient;
import org.openmrs.module.webservices.rest.web.ConversionUtil;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.jms.core.JmsTemplate;

import java.lang.reflect.Method;

public class PatientRegistrationAdvice implements AfterReturningAdvice {

    private final JmsTemplate jmsTemplate;

    public PatientRegistrationAdvice(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] arguments, Object target) {
        String payload = ConversionUtil.convertToRepresentation((Patient)returnValue, Representation.FULL).toString();
        String eventType = "patient-registration";
        this.jmsTemplate.send(eventType, session -> session.createTextMessage(payload));
    }
}