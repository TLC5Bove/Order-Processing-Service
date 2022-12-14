package bove.order.processing.service.orderbook.service;

import bove.order.processing.service.orderbook.model.Orcl;
import bove.order.processing.service.orderbook.repo.OrclPageSortRepo;
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
public class OrclService {

    @Autowired
    OrclPageSortRepo orclPageSortRepo;

    public List<Orcl> findAllPageAndSortBySide (String side, int size) {
        Pageable p;
        if (Objects.equals(side, "BUY")){
            p = PageRequest.of(0, size, Sort.by("price").ascending());

        }else{
            p = PageRequest.of(0, size, Sort.by("price").descending());
        }
        Page<Orcl> page = orclPageSortRepo.findAllBySideAndOrderType(side, "LIMIT", p);

        if(page.hasContent())
            return page.getContent();
        else
            return new ArrayList<Orcl>();
    }
}
