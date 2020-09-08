package guru.springframework.services;

import guru.springframework.common.model.BeerOrderDto;

public interface BeerOrderAllocationService {

    Boolean allocateOrder(BeerOrderDto beerOrderDto);
}
