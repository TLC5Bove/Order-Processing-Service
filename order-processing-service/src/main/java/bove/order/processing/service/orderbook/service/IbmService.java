package bove.order.processing.service.orderbook.service;

import bove.order.processing.service.orderbook.model.Ibm;
import bove.order.processing.service.orderbook.repo.IbmPageSortRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class IbmService {

    @Autowired
    IbmPageSortRepo ibmPageSortRepo;

    public List<Ibm> findAllPageAndSortBySide (String side, int size) {
        Pageable p;

        if (Objects.equals(side, "BUY")){
            p = PageRequest.of(0, size, Sort.by("price").ascending());
            Page<Ibm> page = ibmPageSortRepo.findAllBySideAndOrderType("SELL", "LIMIT", p);

            if(page.hasContent()) return page.getContent();
        } else {
            p = PageRequest.of(0, size, Sort.by("price").descending());
            Page<Ibm> page = ibmPageSortRepo.findAllBySideAndOrderType("BUY", "LIMIT", p);

            if(page.hasContent()) return page.getContent();
        }
        return new ArrayList<Ibm>();
    }
}
