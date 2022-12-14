package bove.order.processing.service.orderbook.service;

import bove.order.processing.service.orderbook.model.Tsla;
import bove.order.processing.service.orderbook.repo.TslaPageSortRepo;
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
public class TslaService {

    @Autowired
    TslaPageSortRepo tslaPageSortRepo;

    public List<Tsla> findAllPageAndSortBySide (String side, int size) {
        Pageable p = PageRequest.of(0, size, Sort.by("price"));
        if (Objects.equals(side, "BUY")){
            p = PageRequest.of(0, size, Sort.by("price").ascending());

        }else{
            p = PageRequest.of(0, size, Sort.by("price").descending());
        }
        Page<Tsla> page = tslaPageSortRepo.findAllBySideAndOrderType(side, "LIMIT", p);

        if(page.hasContent())
            return page.getContent();
        else
            return new ArrayList<Tsla>();
    }
}
