package com.renault.garagesapi.test.service;

import com.renault.garagesapi.dto.AccessoireDto;
import com.renault.garagesapi.dto.VehiculeDto;
import com.renault.garagesapi.entity.Accessoire;
import com.renault.garagesapi.enums.TypeCarburant;
import com.renault.garagesapi.mapper.AccessoireMapper;
import com.renault.garagesapi.mapper.VehiculeMapper;
import com.renault.garagesapi.repository.AccessoireRepository;
import com.renault.garagesapi.service.IVehiculeService;
import com.renault.garagesapi.service.impl.AccessoireServiceImpl;
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
    private AccessoireRepository accessoireRepository;

    @Mock
    private AccessoireMapper accessoireMapper;

    @InjectMocks
    private AccessoireServiceImpl accessoireService;

    @Mock
    private IVehiculeService vehiculeService;

    @Mock
    private VehiculeMapper vehiculeMapper;

    private AccessoireDto accessoireDto;
    private Accessoire accessoire;
    private VehiculeDto vehiculeDto;

    @BeforeEach
    public void setUp() {
        accessoireDto = createAccessoireDto();
        accessoire = createAccessoire();

        vehiculeDto = new VehiculeDto(
                null,
                "Renoult clio 5",
                Year.of(2018),
                TypeCarburant.ESSENCE,
                null);
    }

    @Test
    public void testAddAccessoire() {

        Accessoire accessoire = createAccessoire();
        AccessoireDto accessoireDto = createAccessoireDto();

        when(accessoireMapper.toEntity(accessoireDto)).thenReturn(accessoire);
        when(accessoireRepository.save(accessoire)).thenReturn(accessoire);
        when(accessoireMapper.toDto(accessoire)).thenReturn(accessoireDto);

        AccessoireDto result = accessoireService.addAccessoire(accessoireDto);

        assertThat(accessoireDto.nom()).isEqualTo(result.nom());
        assertThat(accessoireDto.description()).isEqualTo(result.description());
        assertThat(accessoireDto.prix()).isEqualTo(result.prix());
        assertThat(accessoireDto.type()).isEqualTo(result.type());

        verify(accessoireRepository).save(accessoire);
    }

    @Test
    public void testUpdateAccessoire() {
        when(accessoireRepository.findById(1L)).thenReturn(Optional.of(accessoire));
        when(accessoireMapper.toEntity(accessoireDto)).thenReturn(accessoire);
        when(accessoireRepository.save(accessoire)).thenReturn(accessoire);
        when(accessoireMapper.toDto(accessoire)).thenReturn(accessoireDto);

        AccessoireDto result = accessoireService.updateAccessoire(1L, accessoireDto);

        assertNotNull(result);
        verify(accessoireRepository).save(accessoire);
    }

    @Test
    public void testDeleteAccessoire() {
        when(accessoireRepository.findById(1L)).thenReturn(Optional.of(accessoire));

        accessoireService.deleteAccessoire(1L);

        verify(accessoireRepository).delete(accessoire);
    }

    public AccessoireDto createAccessoireDto(){
        AccessoireDto accessoireDto = new AccessoireDto(
                null,
                "Caméra de recul",
                "Caméra de recul",
                700,
                "Sécurité"
        );
        return accessoireDto;
    }

    public Accessoire createAccessoire(){
        Accessoire accessoire = new Accessoire();
        accessoire.setId(1L);
        accessoire.setNom("Caméra de recul");
        accessoire.setDescription("Caméra de recul");
        accessoire.setPrix(new BigDecimal(700));
        accessoire.setType("Sécurité");

        return accessoire;
    }
}
