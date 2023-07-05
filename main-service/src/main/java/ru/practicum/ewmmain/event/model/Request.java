package ru.practicum.ewmmain.event.model;

import lombok.*;
import ru.practicum.ewmmain.event.enums.RequestStatus;
import ru.practicum.ewmmain.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "requests")
public class Request {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created")
    private LocalDateTime created;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", referencedColumnName = "id")
    private User requester;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}
