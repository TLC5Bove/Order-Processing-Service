package bove.order.processing.service.orderbook.repo;

import bove.order.processing.service.orderbook.model.Googl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GooglPageSortRepo extends PagingAndSortingRepository<Googl, String> {
    Page<Googl> findAllBySideAndOrderType(String side, String type, Pageable p);
}
