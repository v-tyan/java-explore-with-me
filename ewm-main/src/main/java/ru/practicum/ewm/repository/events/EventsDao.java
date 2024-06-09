package ru.practicum.ewm.repository.events;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.model.events.Event;
import ru.practicum.ewm.model.events.State;

@Repository
@RequiredArgsConstructor
public class EventsDao {
    private final EntityManager entityManager;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<Event> findEvents(List<Integer> users,
            List<State> states,
            List<Integer> categories,
            String rangeStart,
            String rangeEnd,
            Integer from,
            Integer size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> eventCriteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> eventRoot = eventCriteriaQuery.from(Event.class);
        List<Predicate> predicateList = new ArrayList<>();

        TypedQuery<Event> typedQuery = entityManager.createQuery(eventCriteriaQuery);
        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(size);

        if (users != null) {
            predicateList.add(criteriaBuilder.and(eventRoot.get("initiator").get("id").in(users)));
        }

        if (categories != null) {
            predicateList.add(criteriaBuilder.and(eventRoot.get("category").get("id").in(categories)));
        }

        if (states != null) {
            predicateList.add(criteriaBuilder.and(states.stream()
                    .map(eventState -> criteriaBuilder.equal(eventRoot.get("state"), eventState))
                    .toArray(Predicate[]::new)));
        }

        if (rangeStart != null && rangeEnd != null) {
            LocalDateTime start = LocalDateTime.parse(rangeStart, FORMATTER);
            LocalDateTime end = LocalDateTime.parse(rangeEnd, FORMATTER);
            predicateList.add(criteriaBuilder.and(criteriaBuilder.between(eventRoot.get("eventDate"), start, end)));
        }

        return entityManager.createQuery(eventCriteriaQuery.select(eventRoot)
                .where(predicateList.toArray(Predicate[]::new))).getResultList();
    }
}
