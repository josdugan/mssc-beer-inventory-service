package guru.springframework.services.listeners;

import guru.springframework.config.JMSConfig;
import guru.springframework.domain.BeerInventory;
import guru.springframework.common.events.NewInventoryEvent;
import guru.springframework.repositories.BeerInventoryRepository;
import guru.springframework.common.model.BeerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NewInventoryListener {

    private final BeerInventoryRepository beerInventoryRepository;

    @JmsListener(destination = JMSConfig.NEW_INVENTORY_QUEUE)
    public void listen(NewInventoryEvent newInventoryEvent) {
        log.debug("In NewInventoryListener#listen");

        BeerDto beerDto = newInventoryEvent.getBeerDto();

        BeerInventory beerInventory = beerInventoryRepository.findAllByBeerId(beerDto.getId()).stream().findFirst().get();

        log.debug(String.format("Beer inventory for %1s was %2s", beerDto.getBeerName(), beerInventory.getQuantityOnHand()));

        if (beerInventory != null) {
            beerInventory.addInventory(beerDto.getQuantityOnHand());
        } else {
            beerInventory = BeerInventory.builder()
                    .beerId(beerDto.getId())
                    .upc(beerDto.getUpc())
                    .quantityOnHand(beerDto.getQuantityOnHand())
                    .build();
        }

        log.debug(String.format("AFter brew event, beer inventory for %1s is %2s", beerDto.getBeerName(), beerInventory.getQuantityOnHand()));

        beerInventoryRepository.save(beerInventory);
    }
}
