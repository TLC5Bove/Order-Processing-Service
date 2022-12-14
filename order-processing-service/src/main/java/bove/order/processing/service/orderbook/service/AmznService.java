package bove.order.processing.service.orderbook.service;

import bove.order.processing.service.orderbook.model.Amzn;
import bove.order.processing.service.orderbook.model.Stock;
import bove.order.processing.service.orderbook.repo.AmznPageSortRepo;
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
public class AmznService {
    @Autowired
    AmznPageSortRepo amznPageSortRepo;

    public List<Amzn> findAllPageAndSortBySide (String side, int size) {
        Pageable p;
        if (Objects.equals(side, "BUY")){
            p = PageRequest.of(0, size, Sort.by("price").ascending());

        }else{
            p = PageRequest.of(0, size, Sort.by("price").descending());
        }
        Page<Amzn> page = amznPageSortRepo.findAllBySideAndOrderType(side, "LIMIT", p);

        if(page.hasContent())
            return page.getContent();
        else
            return new ArrayList<Amzn>();
    }

}
