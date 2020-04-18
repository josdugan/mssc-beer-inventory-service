package guru.springframework.bootstrap;

import guru.springframework.domain.BeerInventory;
import guru.springframework.repositories.BeerInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerInventoryBootstrap implements CommandLineRunner {

    public static final String BEER_1_UPC = "01234567890";    
    public static final String BEER_2_UPC = "01234567891";
    public static final String BEER_3_UPC = "01234567892";
    
    public static final UUID BEER_1_UUID = UUID.randomUUID();
    public static final UUID BEER_2_UUID = UUID.randomUUID();
    public static final UUID BEER_3_UUID = UUID.randomUUID();

    private final BeerInventoryRepository beerInventoryRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (beerInventoryRepository.count() == 0) {
            loadInitialInventory();
        }
    }

    private void loadInitialInventory() {
        beerInventoryRepository.save(BeerInventory.builder()
            .beerId(BEER_1_UUID)
                .upc(BEER_1_UPC)
                .quantityOnHand(50)
                .build());

        beerInventoryRepository.save(BeerInventory.builder()
            .beerId(BEER_2_UUID)
                .upc(BEER_2_UPC)
                .quantityOnHand(50)
                .build()
        );

        beerInventoryRepository.saveAndFlush(BeerInventory.builder()
            .beerId(BEER_3_UUID)
                .upc(BEER_3_UPC)
                .quantityOnHand(50)
                .build()
        );

        log.debug("Loaded Inventory. Record count: " + beerInventoryRepository.count());
    }
}
