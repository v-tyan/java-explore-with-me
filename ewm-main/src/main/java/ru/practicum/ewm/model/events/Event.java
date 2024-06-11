package ru.practicum.ewm.model.events;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.model.categories.Category;
import ru.practicum.ewm.model.location.Location;
import ru.practicum.ewm.model.users.User;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "EVENTS")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "annotation", nullable = false, length = 2000)
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE NOT NULL")
    private LocalDateTime createdOn;

    @Column(length = 7000)
    private String description;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE NOT NULL")
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean paid;

    @Column(columnDefinition = "Integer DEFAULT 0")
    private Long participantLimit;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE NOT NULL")
    private LocalDateTime publishedOn;

    @Column(columnDefinition = "Boolean DEFAULT TRUE")
    private Boolean requestModeration;

    @Column(length = 20, columnDefinition = "varchar(20) DEFAULT 'PENDING'")
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(nullable = false, length = 500)
    private String title;

    @Transient
    private int confirmedRequests;

}
