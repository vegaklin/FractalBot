package ru.kfu.fractal.repository.orm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "configurations")
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "affine_count", nullable = false)
    private Long affineCount;

    @Column(name = "symmetry_count", nullable = false)
    private Long symmetryCount;

    @Column(name = "samples", nullable = false)
    private Long samples;

    @Column(name = "iterations", nullable = false)
    private Long iterations;

    @Column(name = "image_width", nullable = false)
    private Long imageWidth;

    @Column(name = "image_height", nullable = false)
    private Long imageHeight;

    @Column(name = "rect_x", nullable = false)
    private Double rectX;

    @Column(name = "rect_y", nullable = false)
    private Double rectY;

    @Column(name = "rect_width", nullable = false)
    private Double rectWidth;

    @Column(name = "rect_height", nullable = false)
    private Double rectHeight;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "transformation_types")
    private List<String> transformationTypes;
}

