package bove.order.processing.service.orderbook.repo;

import bove.order.processing.service.orderbook.model.Tsla;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TslaPageSortRepo extends PagingAndSortingRepository<Tsla, String> {
    Page<Tsla> findAllBySideAndOrderType(String side, String type, Pageable p);
}
