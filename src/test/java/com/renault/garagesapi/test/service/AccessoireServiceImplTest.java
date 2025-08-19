package com.renault.garagesapi.test.service;

import com.renault.garagesapi.dto.AccessoryDto;
import com.renault.garagesapi.dto.VehicleDto;
import com.renault.garagesapi.entity.Accessory;
import com.renault.garagesapi.enums.TypeCarburant;
import com.renault.garagesapi.mapper.AccessoryMapper;
import com.renault.garagesapi.mapper.VehicleMapper;
import com.renault.garagesapi.repository.AccessoryRepository;
import com.renault.garagesapi.service.IVehicleService;
import com.renault.garagesapi.service.impl.AccessoryServiceImpl;
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
                TypeCarburant.ESSENCE,
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

        assertThat(accessoryDto.nom()).isEqualTo(result.nom());
        assertThat(accessoryDto.description()).isEqualTo(result.description());
        assertThat(accessoryDto.prix()).isEqualTo(result.prix());
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
        accessory.setNom("Caméra de recul");
        accessory.setDescription("Caméra de recul");
        accessory.setPrix(new BigDecimal(700));
        accessory.setType("Sécurité");

        return accessory;
    }
}
