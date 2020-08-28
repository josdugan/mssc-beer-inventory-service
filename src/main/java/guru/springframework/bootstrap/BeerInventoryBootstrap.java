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
    
    public static final UUID BEER_1_UUID = UUID.fromString("e7bd7a37-d392-4074-8ca2-c80ba7f9e5ee");
    public static final UUID BEER_2_UUID = UUID.fromString("017235b7-3d29-43e1-9a00-3057c41a0bb6");
    public static final UUID BEER_3_UUID = UUID.fromString("33e6c476-1e80-478f-a64b-23524c0219e4");

    public static final int QUANTITY_ON_HAND = 1;

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
                .quantityOnHand(QUANTITY_ON_HAND)
                .build());

        beerInventoryRepository.save(BeerInventory.builder()
            .beerId(BEER_2_UUID)
                .upc(BEER_2_UPC)
                .quantityOnHand(QUANTITY_ON_HAND)
                .build()
        );

        beerInventoryRepository.saveAndFlush(BeerInventory.builder()
            .beerId(BEER_3_UUID)
                .upc(BEER_3_UPC)
                .quantityOnHand(QUANTITY_ON_HAND)
                .build()
        );

        log.debug("Loaded Inventory. Record count: " + beerInventoryRepository.count());
    }
}
