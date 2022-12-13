package bove.order.processing.service.orderbook.repo;

import bove.order.processing.service.orderbook.model.Nflx;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NflxPageSortRepo extends PagingAndSortingRepository<Nflx, String> {
    Page<Nflx> findAllBySideAndOrderType(String side, String type, Pageable p);
}
