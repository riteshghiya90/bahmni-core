package org.bahmni.module.referencedata.labconcepts.mapper;

import org.bahmni.module.referencedata.labconcepts.contract.Resource;
import org.openmrs.Concept;
import org.openmrs.ConceptSet;
import org.openmrs.api.context.Context;

import java.util.List;

public abstract class ResourceMapper {
    public static final double DEFAULT_SORT_ORDER = 999.0;
    String parentConceptName;

    protected ResourceMapper(String parentConceptName) {
        this.parentConceptName = parentConceptName;
    }

    public abstract <T extends Resource> T map(Concept concept);


    <T extends Resource> T mapResource(Resource resource, Concept concept) {
        resource.setName(concept.getName(Context.getLocale()).getName());
        resource.setIsActive(!concept.isRetired());
        resource.setId(concept.getUuid());
        resource.setDateCreated(concept.getDateCreated());
        resource.setLastUpdated(concept.getDateChanged());
        resource.setSortOrder(getSortWeight(concept));
        return (T) resource;
    }

    Double getSortWeight(Concept concept) {
        List<ConceptSet> conceptSets = Context.getConceptService().getSetsContainingConcept(concept);
        if (conceptSets == null) return DEFAULT_SORT_ORDER;
        for (ConceptSet conceptSet : conceptSets) {
            if (conceptSet.getConceptSet().getName(Context.getLocale()).getName().equals(parentConceptName)) {
                return conceptSet.getSortWeight() != DEFAULT_SORT_ORDER ? conceptSet.getSortWeight() : Double.valueOf(999);
            }
        }
        return DEFAULT_SORT_ORDER;
    }

}
