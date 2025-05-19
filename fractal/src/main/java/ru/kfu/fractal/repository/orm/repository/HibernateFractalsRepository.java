package ru.kfu.fractal.repository.orm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kfu.fractal.repository.orm.entity.Fractal;

@Repository
public interface HibernateFractalsRepository extends JpaRepository<Fractal, Long> {
}