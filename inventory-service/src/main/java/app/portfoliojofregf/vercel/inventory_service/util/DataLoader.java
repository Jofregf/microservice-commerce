package app.portfoliojofregf.vercel.inventory_service.util;

import app.portfoliojofregf.vercel.inventory_service.model.entity.Inventory;
import app.portfoliojofregf.vercel.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final InventoryRepository inventoryRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Loading data...");
        if(inventoryRepository.findAll().size() == 0) {
            inventoryRepository.saveAll(
                    List.of(
                            Inventory.builder().sku("Galaxy A04").quantity(15L).build(),
                            Inventory.builder().sku("Moto e13").quantity(70L).build(),
                            Inventory.builder().sku("Note 11").quantity(14L).build(),
                            Inventory.builder().sku("TCL 403").quantity(0L).build()
                    )
            );
        }
        log.info("Data loaded..");
    }
}
