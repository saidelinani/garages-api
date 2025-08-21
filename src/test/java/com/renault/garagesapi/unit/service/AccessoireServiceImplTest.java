package com.renault.garagesapi.tests.service;

import com.renault.garagesapi.dtos.AccessoryDto;
import com.renault.garagesapi.dtos.VehicleDto;
import com.renault.garagesapi.entities.Accessory;
import com.renault.garagesapi.enums.FuelType;
import com.renault.garagesapi.mappers.AccessoryMapper;
import com.renault.garagesapi.mappers.VehicleMapper;
import com.renault.garagesapi.repositories.AccessoryRepository;
import com.renault.garagesapi.services.IVehicleService;
import com.renault.garagesapi.services.impl.AccessoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.Year;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccessoireServiceImplTest {

    @Mock
    private AccessoryRepository accessoryRepository;

    @Mock
    private AccessoryMapper accessoryMapper;

    @InjectMocks
    private AccessoryServiceImpl accessoireService;

    @Mock
    private IVehicleService vehiculeService;

    @Mock
    private VehicleMapper vehicleMapper;

    private AccessoryDto accessoryDto;
    private Accessory accessory;
    private VehicleDto vehicleDto;

    @BeforeEach
    public void setUp() {
        accessoryDto = createAccessoireDto();
        accessory = createAccessoire();

        vehicleDto = new VehicleDto(
                null,
                "Renoult clio 5",
                Year.of(2018),
                FuelType.ESSENCE,
                null);
    }

    @Test
    public void testAddAccessoire() {

        Accessory accessory = createAccessoire();
        AccessoryDto accessoryDto = createAccessoireDto();

        when(accessoryMapper.toEntity(accessoryDto)).thenReturn(accessory);
        when(accessoryRepository.save(accessory)).thenReturn(accessory);
        when(accessoryMapper.toDto(accessory)).thenReturn(accessoryDto);

        AccessoryDto result = accessoireService.addAccessoire(accessoryDto);

        assertThat(accessoryDto.name()).isEqualTo(result.name());
        assertThat(accessoryDto.description()).isEqualTo(result.description());
        assertThat(accessoryDto.price()).isEqualTo(result.price());
        assertThat(accessoryDto.type()).isEqualTo(result.type());

        verify(accessoryRepository).save(accessory);
    }

    @Test
    public void testUpdateAccessoire() {
        when(accessoryRepository.findById(1L)).thenReturn(Optional.of(accessory));
        when(accessoryMapper.toEntity(accessoryDto)).thenReturn(accessory);
        when(accessoryRepository.save(accessory)).thenReturn(accessory);
        when(accessoryMapper.toDto(accessory)).thenReturn(accessoryDto);

        AccessoryDto result = accessoireService.updateAccessoire(1L, accessoryDto);

        assertNotNull(result);
        verify(accessoryRepository).save(accessory);
    }

    @Test
    public void testDeleteAccessoire() {
        when(accessoryRepository.findById(1L)).thenReturn(Optional.of(accessory));

        accessoireService.deleteAccessoire(1L);

        verify(accessoryRepository).delete(accessory);
    }

    public AccessoryDto createAccessoireDto(){
        AccessoryDto accessoryDto = new AccessoryDto(
                null,
                "Caméra de recul",
                "Caméra de recul",
                700,
                "Sécurité"
        );
        return accessoryDto;
    }

    public Accessory createAccessoire(){
        Accessory accessory = new Accessory();
        accessory.setId(1L);
        accessory.setName("Caméra de recul");
        accessory.setDescription("Caméra de recul");
        accessory.setPrice(new BigDecimal(700));
        accessory.setType("Sécurité");

        return accessory;
    }
}
