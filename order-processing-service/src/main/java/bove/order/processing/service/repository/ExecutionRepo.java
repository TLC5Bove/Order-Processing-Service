package bove.order.processing.service.repository;

import bove.order.processing.service.dto.order.Execution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutionRepo extends JpaRepository<Execution, Integer> {
}
