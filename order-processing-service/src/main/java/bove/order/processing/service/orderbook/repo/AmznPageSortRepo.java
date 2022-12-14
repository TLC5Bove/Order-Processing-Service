package bove.order.processing.service.orderbook.repo;

import bove.order.processing.service.orderbook.model.Amzn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmznPageSortRepo extends PagingAndSortingRepository<Amzn, String> {
    Page<Amzn> findAllBySideAndOrderType(String side, String type, Pageable p);
}
