package bove.order.processing.service.orderbook.repo;

import bove.order.processing.service.orderbook.model.Ibm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IbmPageSortRepo extends PagingAndSortingRepository<Ibm, String> {
    Page<Ibm> findAllBySide(String side, Pageable p);

    Page<Ibm> findAllBySideAndOrderType(String side, String type, Pageable p);
}
