package ru.kfu.fractal.repository.orm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kfu.fractal.repository.orm.entity.Configuration;

@Repository
public interface HibernateConfigurationsRepository extends JpaRepository<Configuration, Long> {
}