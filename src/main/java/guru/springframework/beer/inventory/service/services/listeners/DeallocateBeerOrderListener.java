package guru.springframework.beer.inventory.service.services.listeners;

import guru.springframework.beer.inventory.service.config.JMSConfig;
import guru.springframework.beer.inventory.service.services.BeerOrderAllocationService;
import guru.springframework.brewery.events.DeallocateOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeallocateBeerOrderListener {

    private final BeerOrderAllocationService beerOrderAllocationService;

    @JmsListener(destination = JMSConfig.DEALLOCATE_ORDER_QUEUE)
    public void listen(DeallocateOrderRequest request) {
        beerOrderAllocationService.deallocateOrder(request.getBeerOrderDto());
    }
}
