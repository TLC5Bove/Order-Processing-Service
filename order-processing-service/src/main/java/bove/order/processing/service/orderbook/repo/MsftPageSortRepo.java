package bove.order.processing.service.orderbook.repo;

import bove.order.processing.service.orderbook.model.Msft;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MsftPageSortRepo extends PagingAndSortingRepository<Msft, String> {
    Page<Msft> findAllBySideAndOrderType(String side, String type, Pageable p);
}