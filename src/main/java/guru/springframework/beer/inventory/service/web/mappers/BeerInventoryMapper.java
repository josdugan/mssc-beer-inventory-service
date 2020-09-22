package guru.springframework.beer.inventory.service.web.mappers;

import guru.springframework.beer.inventory.service.domain.BeerInventory;
import guru.springframework.brewery.model.BeerInventoryDto;
import org.mapstruct.Mapper;

@Mapper(uses = DateMapper.class)
public interface BeerInventoryMapper {

    BeerInventory beerInventoryDtoToBeerInventory(BeerInventoryDto beerInventoryDto);

    BeerInventoryDto beerInventoryToBeerInventoryDto(BeerInventory beerInventory);
}
