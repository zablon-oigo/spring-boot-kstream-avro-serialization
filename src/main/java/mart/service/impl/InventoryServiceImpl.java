package mart.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import mart.avro.InventoryEvent;
import mart.domain.dto.InventoryDto;
import mart.domain.dto.InventoryRequestDto;
import mart.domain.dto.UpdateRequestDto;
import mart.domain.entity.Inventory;
import mart.domain.entity.Status;
import mart.exception.InventoryNotFoundException;
import mart.mapper.InventoryMapper;
import mart.producer.InventoryProducer;
import mart.repository.InventoryRepository;
import mart.service.InventoryService;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final InventoryProducer inventoryProducer;

    public InventoryServiceImpl(
            InventoryRepository inventoryRepository,
            InventoryMapper inventoryMapper,
            InventoryProducer inventoryProducer
    ) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
        this.inventoryProducer = inventoryProducer;
    }

    // CREATE
    @Override
    public InventoryDto createInventory(InventoryRequestDto request) {

        Inventory inventory = inventoryMapper.toEntity(request);

        inventory.setStatus(Status.IN_STOCK);
        inventory.setCreated(Instant.now());
        inventory.setUpdated(Instant.now());

        Inventory saved = inventoryRepository.save(inventory);

        InventoryDto dto = inventoryMapper.toDto(saved);

        InventoryEvent event = InventoryEvent.newBuilder()
                .setInventoryId(saved.getId().toString())
                .setName(saved.getName())
                .setCategory(saved.getCategory())
                .setQuantity(saved.getQuantity())
                .setPrice(saved.getPrice())
                .setEventType(mart.avro.EventType.CREATED)
                .setTimestamp(Instant.now().toString())
                .build();
        inventoryProducer.sendEvent(event);

        return dto;
    }

    // LIST 
    @Override
    public List<InventoryDto> listInventories() {

        return inventoryRepository.findAll(Sort.by(Sort.Direction.DESC, "created"))
                .stream()
                .map(inventoryMapper::toDto)
                .toList();
    }

    // UPDATE 
    @Override
    public InventoryDto updateInventory(UUID inventoryId, UpdateRequestDto request) {

        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));

        if (request.name() != null) inventory.setName(request.name());
        if (request.category() != null) inventory.setCategory(request.category());
        if (request.quantity() != null) inventory.setQuantity(request.quantity());
        if (request.price() != null) inventory.setPrice(request.price());
        if (request.status() != null) inventory.setStatus(request.status());

        inventory.setUpdated(Instant.now());

        Inventory saved = inventoryRepository.save(inventory);

        InventoryDto dto = inventoryMapper.toDto(saved);

        InventoryEvent event = InventoryEvent.newBuilder()
                .setInventoryId(saved.getId().toString())
                .setName(saved.getName())
                .setCategory(saved.getCategory())
                .setQuantity(saved.getQuantity())
                .setPrice(saved.getPrice())
                .setEventType(mart.avro.EventType.UPDATED)
                .setTimestamp(Instant.now().toString())
                .build();

        inventoryProducer.sendEvent(event);

        return dto;
    }

    // SELL 
    @Override
    public InventoryDto sellProduct(UUID id, int quantity) {

        Inventory inv = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException(id));

        if (inv.getQuantity() < quantity) {
            throw new RuntimeException("Not enough stock");
        }

        int newQty = inv.getQuantity() - quantity;
        inv.setQuantity(newQty);
        inv.setStatus(newQty == 0 ? Status.OUT_OF_STOCK : Status.IN_STOCK);
        inv.setUpdated(Instant.now());

        Inventory saved = inventoryRepository.save(inv);

        InventoryDto dto = inventoryMapper.toDto(saved);

        InventoryEvent event = InventoryEvent.newBuilder()
                .setInventoryId(saved.getId().toString())
                .setName(saved.getName())
                .setCategory(saved.getCategory())
                .setQuantity(saved.getQuantity())
                .setPrice(saved.getPrice())
                .setEventType(mart.avro.EventType.SOLD)
                .setTimestamp(Instant.now().toString())
                .build();

        inventoryProducer.sendEvent(event);

        return dto;
    }

    // RESTOCK 
    @Override
    public InventoryDto restockProduct(UUID id, int quantity) {

        Inventory inv = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException(id));

        inv.setQuantity(inv.getQuantity() + quantity);
        inv.setStatus(Status.IN_STOCK);
        inv.setUpdated(Instant.now());

        Inventory saved = inventoryRepository.save(inv);

        InventoryDto dto = inventoryMapper.toDto(saved);

        InventoryEvent event = InventoryEvent.newBuilder()
                .setInventoryId(saved.getId().toString())
                .setName(saved.getName())
                .setCategory(saved.getCategory())
                .setQuantity(saved.getQuantity())
                .setPrice(saved.getPrice())
                .setEventType(mart.avro.EventType.RESTOCKED)
                .setTimestamp(Instant.now().toString())
                .build();

        inventoryProducer.sendEvent(event);

        return dto;
    }

    // DELETE 
    @Override
    public void deleteInventory(UUID inventoryId) {

        Inventory inv = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));

        inventoryRepository.deleteById(inventoryId);

        InventoryEvent event = InventoryEvent.newBuilder()
                .setInventoryId(inv.getId().toString())
                .setName(inv.getName())
                .setCategory(inv.getCategory())
                .setQuantity(inv.getQuantity())
                .setPrice(inv.getPrice())
                .setEventType(mart.avro.EventType.DELETED)
                .setTimestamp(Instant.now().toString())
                .build();

        inventoryProducer.sendEvent(event);
    }
}