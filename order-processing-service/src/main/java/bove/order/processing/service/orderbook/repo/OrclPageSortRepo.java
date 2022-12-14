package bove.order.processing.service.orderbook.repo;

import bove.order.processing.service.orderbook.model.Orcl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrclPageSortRepo extends PagingAndSortingRepository<Orcl, String> {
    Page<Orcl> findAllBySideAndOrderType(String side, String type, Pageable p);
}
