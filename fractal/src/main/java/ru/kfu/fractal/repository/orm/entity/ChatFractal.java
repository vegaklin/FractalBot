package ru.kfu.fractal.repository.orm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Table(name = "chat_fractals")
public class ChatFractal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "fractal_id", nullable = false)
    private Fractal fractal;

    @ManyToOne
    @JoinColumn(name = "configuration_id", nullable = false)
    private Configuration configuration;

    @Column(name = "create_time", nullable = false)
    private OffsetDateTime createTime;
}
