package mart.mapper;

import mart.domain.dto.InventoryDto;
import mart.domain.dto.InventoryRequestDto;
import mart.domain.dto.UpdateRequestDto;
import mart.domain.entity.Inventory;

public interface InventoryMapper {

    Inventory toEntity(InventoryRequestDto dto);

    Inventory toEntity(UpdateRequestDto dto);

    InventoryDto toDto(Inventory inventory);
}