package com.renault.garagesapi.test.repository;

import com.renault.garagesapi.entity.Garage;
import com.renault.garagesapi.repository.GarageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class GarageRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GarageRepository garageRepository;

    @Test
    public void testaddGarage() {

        Garage garage = new Garage();
        garage.setName("Garage Renoult");
        garage.setCity("Casablanca");
        garage.setAddress("44, Rue 23");
        garage.setTelephone("0522845265");
        garage.setEmail("contact@renoult.ma");

        Garage savedGarage = garageRepository.save(garage);

        assertThat(savedGarage.getId()).isNotNull();
        assertThat(savedGarage.getName()).isEqualTo("Garage Renoult");
        assertThat(savedGarage.getAddress()).isEqualTo("44, Rue 23");
        assertThat(savedGarage.getTelephone()).isEqualTo("0522845265");
        assertThat(savedGarage.getEmail()).isEqualTo("contact@renoult.ma");
    }

    @Test
    public void testFindAll() throws SQLException {

        Garage garage1 = new Garage();
        garage1.setName("Garage Renoult Casablanca");
        garage1.setCity("Casablanca");
        garage1.setAddress("44, Rue 23");
        garage1.setTelephone("0522845265");
        garage1.setEmail("casa-contact@renoult.ma");

        Garage garage2 = new Garage();
        garage2.setName("Garage Renoult Tanger");
        garage2.setCity("Tanger");
        garage2.setAddress("Hassan II, rue 14");
        garage2.setTelephone("0522845765");
        garage2.setEmail("tanger-contact@renoult.ma");

        entityManager.persist(garage1);
        entityManager.persist(garage2);

        List<Garage> garages = garageRepository.findAll();

        assertThat(garages).hasSize(2);
        assertThat(garages).extracting(Garage::getName)
                .containsExactlyInAnyOrder("Garage Renoult Casablanca", "Garage Renoult Tanger");
        assertThat(garages).extracting(Garage::getCity)
                .containsExactlyInAnyOrder("Casablanca", "Tanger");
    }

    @Test
    public void testDeleteById() {

        Garage garage = new Garage();
        garage.setName("Garage test delete");
        garage.setCity("Casablanca");
        garage.setAddress("Address test delete");
        garage.setTelephone("0522845265");
        garage.setEmail("casa-contact@renoult.ma");

        Garage savedGarage = entityManager.persist(garage);

        garageRepository.deleteById(savedGarage.getId());

        Optional<Garage> deletedGarage = garageRepository.findById(savedGarage.getId());
        assertThat(deletedGarage).isEmpty();
    }

    @Test
    public void testUpdateGarage() {

        Garage garage = new Garage();
        garage.setName("Garage Casa");
        garage.setCity("Casablanca");
        garage.setAddress("44, Rue 23");
        garage.setTelephone("0522845265");
        garage.setEmail("contact@renoult.ma");

        Garage savedGarage = entityManager.persist(garage);

        savedGarage.setName("Garage Bouskoura");
        savedGarage.setAddress("Bouskoura");

        Garage updatedGarage = garageRepository.save(savedGarage);
        entityManager.flush();

        assertThat(updatedGarage.getId()).isEqualTo(savedGarage.getId());
        assertThat(updatedGarage.getName()).isEqualTo("Garage Bouskoura");
        assertThat(updatedGarage.getAddress()).isEqualTo("Bouskoura");
    }
}
