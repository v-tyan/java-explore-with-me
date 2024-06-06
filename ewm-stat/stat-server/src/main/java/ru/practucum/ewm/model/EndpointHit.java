package ru.practucum.ewm.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "HITS")
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp; // "yyyy-MM-dd HH:mm:ss"
}
