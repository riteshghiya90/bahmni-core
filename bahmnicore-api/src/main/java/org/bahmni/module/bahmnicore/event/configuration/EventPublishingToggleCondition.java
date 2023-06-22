package org.bahmni.module.bahmnicore.event.configuration;


import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;

@Component
public class EventPublishingToggleCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String event_publisher_toggle = System.getenv("EVENT_PUBLISH_TOGGLE");
        return  event_publisher_toggle != null && event_publisher_toggle.equalsIgnoreCase("true");
    }
}