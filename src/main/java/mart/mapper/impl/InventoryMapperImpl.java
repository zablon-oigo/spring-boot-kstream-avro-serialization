package mart.mapper.impl;

import org.springframework.stereotype.Component;

import mart.domain.dto.InventoryDto;
import mart.domain.dto.InventoryRequestDto;
import mart.domain.dto.UpdateRequestDto;
import mart.domain.entity.Inventory;
import mart.mapper.InventoryMapper;

@Component
public class InventoryMapperImpl implements InventoryMapper {

    @Override
    public Inventory toEntity(InventoryRequestDto dto){
        Inventory inv = new Inventory();
        inv.setName(dto.name());
        inv.setCategory(dto.category());
        inv.setPrice(dto.price());
        inv.setQuantity(dto.quantity());
        return inv;
    }

    @Override
    public Inventory toEntity(UpdateRequestDto dto){
        Inventory inv = new Inventory();
        inv.setName(dto.name());
        inv.setCategory(dto.category());
        inv.setPrice(dto.price());
        inv.setQuantity(dto.quantity());
        return inv;
    }

    @Override
    public InventoryDto toDto(Inventory inventory){
        return new InventoryDto(
        inventory.getId(),
        inventory.getName(),
        inventory.getCategory(),
        inventory.getQuantity(),
        inventory.getPrice(),
        inventory.getStatus()
);
    }
}