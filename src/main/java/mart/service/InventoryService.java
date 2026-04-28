package mart.service;

import mart.domain.dto.InventoryDto;
import mart.domain.dto.InventoryRequestDto;
import mart.domain.dto.UpdateRequestDto;

import java.util.List;
import java.util.UUID;

public interface InventoryService {

    InventoryDto createInventory(InventoryRequestDto request);

    List<InventoryDto> listInventories();

    InventoryDto updateInventory(UUID inventoryId, UpdateRequestDto request);

    InventoryDto sellProduct(UUID inventoryId, int quantity);

    InventoryDto restockProduct(UUID inventoryId, int quantity);

    void deleteInventory(UUID inventoryId);
}