package guru.springframework.services.listeners;

import guru.springframework.common.events.AllocateBeerOrderRequest;
import guru.springframework.common.events.AllocateBeerOrderResponse;
import guru.springframework.common.model.BeerOrderDto;
import guru.springframework.config.JMSConfig;
import guru.springframework.services.BeerOrderAllocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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
        } catch (Exception e) {
            log.error("Allocation failed for Order Id: " + beerOrderDto.getId());
            responseBuilder.allocationError(true);
        }

        jmsTemplate.convertAndSend(JMSConfig.ALLOCATE_ORDER_RESULT_QUEUE, responseBuilder.build());

        log.debug("Allocation response message sent!");
    }

}
