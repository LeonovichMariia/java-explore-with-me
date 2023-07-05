package ru.practicum.ewmmain.event.model;

import lombok.*;
import ru.practicum.ewmmain.category.model.Category;
import ru.practicum.ewmmain.compilation.model.Compilation;
import ru.practicum.ewmmain.event.enums.EventState;
import ru.practicum.ewmmain.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "events")
public class Event {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "annotation", nullable = false, length = 2000)
    private String annotation;
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests = 0;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
    @Column(name = "description", length = 7000)
    private String description;
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
    @JoinColumn(name = "initiator_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User initiator;
    @Column(name = "lat")
    private Float lat;
    @Column(name = "lon")
    private Float lon;
    @Column(name = "paid")
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventState state;
    @Column(name = "title")
    private String title;
    @ManyToMany(mappedBy = "events")
    private List<Compilation> compilations;
    @Transient
    private Long views;
}
