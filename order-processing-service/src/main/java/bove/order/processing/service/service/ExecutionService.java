package bove.order.processing.service.service;

import bove.order.processing.service.dto.order.Execution;
import bove.order.processing.service.repository.ExecutionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExecutionService {
    @Autowired
    ExecutionRepo executionRepo;

    public void save(Execution execution) {
        executionRepo.save(execution);
    }
}

