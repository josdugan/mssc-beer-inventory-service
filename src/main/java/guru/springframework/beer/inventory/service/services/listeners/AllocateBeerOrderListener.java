package guru.springframework.beer.inventory.service.services.listeners;

import guru.springframework.brewery.events.AllocateBeerOrderRequest;
import guru.springframework.brewery.events.AllocateBeerOrderResponse;
import guru.springframework.brewery.model.BeerOrderDto;
import guru.springframework.beer.inventory.service.config.JMSConfig;
import guru.springframework.beer.inventory.service.services.BeerOrderAllocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AllocateBeerOrderListener {

    private final BeerOrderAllocationService beerOrderAllocationService;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JMSConfig.ALLOCATE_ORDER_QUEUE)
    public void listen(AllocateBeerOrderRequest allocateBeerOrderRequest) {
        BeerOrderDto beerOrderDto = allocateBeerOrderRequest.getBeerOrderDto();

        log.debug("Allocation request message received for order: " + beerOrderDto.getId());

        AllocateBeerOrderResponse.AllocateBeerOrderResponseBuilder responseBuilder = AllocateBeerOrderResponse.builder();
        responseBuilder.beerOrderDto(beerOrderDto);

        try {
            Boolean isFullyAllocated = beerOrderAllocationService.allocateOrder(beerOrderDto);

            if (isFullyAllocated) {
                responseBuilder.pendingInventory(false);
            } else {
                responseBuilder.pendingInventory(true);
            }

            responseBuilder.allocationError(false);
        } catch (Exception e) {
            log.error("Allocation failed for Order Id: " + beerOrderDto.getId());
            responseBuilder.allocationError(true);
        }

        jmsTemplate.convertAndSend(JMSConfig.ALLOCATE_ORDER_RESULT_QUEUE, responseBuilder.build());

        log.debug("Allocation response message sent!");
    }

}
