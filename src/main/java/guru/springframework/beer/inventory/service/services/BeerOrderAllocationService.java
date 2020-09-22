package guru.springframework.beer.inventory.service.services;

import guru.springframework.brewery.model.BeerOrderDto;

public interface BeerOrderAllocationService {

    Boolean allocateOrder(BeerOrderDto beerOrderDto);

    void deallocateOrder(BeerOrderDto beerOrderDto);
}
