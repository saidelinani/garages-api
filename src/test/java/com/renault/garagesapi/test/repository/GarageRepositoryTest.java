package com.renault.garagesapi.test.repository;

import com.renault.garagesapi.entity.Garage;
import com.renault.garagesapi.repository.GarageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class GarageRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DataSource dataSource;


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
        entityManager.flush();

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

        entityManager.persistAndFlush(garage1);
        entityManager.persistAndFlush(garage2);

        List<Garage> garages = garageRepository.findAll();

        assertThat(garages).hasSize(2);
        assertThat(garages).extracting(Garage::getName)
                .containsExactlyInAnyOrder("Garage Renoult Casablanca", "Garage Renoult Tanger");
        assertThat(garages).extracting(Garage::getCity)
                .containsExactlyInAnyOrder("Casablanca", "Tanger");
    }

    @Test
    @DisplayName("Devrait supprimer un garage par son ID")
    public void testDeleteById() {

        Garage garage = new Garage();
        garage.setName("Garage test delete");
        garage.setCity("Casablanca");
        garage.setAddress("Address test delete");
        garage.setTelephone("0522845265");

        Garage savedGarage = entityManager.persistAndFlush(garage);

        garageRepository.deleteById(savedGarage.getId());
        entityManager.flush();

        Optional<Garage> deletedGarage = garageRepository.findById(savedGarage.getId());
        assertThat(deletedGarage).isEmpty();
    }
//
//    @Test
//    @DisplayName("Devrait trouver les garages par ville")
//    public void testFindByVille() {
//        // Given
//        Garage garage1 = new Garage();
//        garage1.setNom("Garage Paris 1");
//        garage1.setVille("Paris");
//        garage1.setAdresse("10 Rue de Rivoli");
//
//        Garage garage2 = new Garage();
//        garage2.setNom("Garage Paris 2");
//        garage2.setVille("Paris");
//        garage2.setAdresse("20 Champs Élysées");
//
//        Garage garage3 = new Garage();
//        garage3.setNom("Garage Lyon");
//        garage3.setVille("Lyon");
//        garage3.setAdresse("30 Rue de la République");
//
//        entityManager.persistAndFlush(garage1);
//        entityManager.persistAndFlush(garage2);
//        entityManager.persistAndFlush(garage3);
//
//        // When
//        List<Garage> garagesParis = garageRepository.findByVille("Paris");
//
//        // Then
//        assertThat(garagesParis).hasSize(2);
//        assertThat(garagesParis).extracting(Garage::getNom)
//                .containsExactlyInAnyOrder("Garage Paris 1", "Garage Paris 2");
//    }
//
//    @Test
//    @DisplayName("Devrait trouver les garages par code postal")
//    public void testFindByCodePostal() {
//        // Given
//        Garage garage = new Garage();
//        garage.setNom("Garage 75001");
//        garage.setCodePostal("75001");
//        garage.setAdresse("1 Place Vendôme");
//        entityManager.persistAndFlush(garage);
//
//        // When
//        List<Garage> garages = garageRepository.findByCodePostal("75001");
//
//        // Then
//        assertThat(garages).hasSize(1);
//        assertThat(garages.get(0).getNom()).isEqualTo("Garage 75001");
//    }
//
//    @Test
//    @DisplayName("Devrait compter le nombre de garages")
//    public void testCount() {
//        // Given
//        Garage garage1 = new Garage();
//        garage1.setNom("Garage 1");
//
//        Garage garage2 = new Garage();
//        garage2.setNom("Garage 2");
//
//        entityManager.persistAndFlush(garage1);
//        entityManager.persistAndFlush(garage2);
//
//        // When
//        long count = garageRepository.count();
//
//        // Then
//        assertThat(count).isEqualTo(2);
//    }
//
//    @Test
//    @DisplayName("Devrait vérifier l'existence d'un garage par son nom")
//    public void testExistsByNom() {
//        // Given
//        Garage garage = new Garage();
//        garage.setNom("Garage Unique");
//        garage.setAdresse("123 Rue Unique");
//        entityManager.persistAndFlush(garage);
//
//        // When & Then
//        assertThat(garageRepository.existsByNom("Garage Unique")).isTrue();
//        assertThat(garageRepository.existsByNom("Garage Inexistant")).isFalse();
//    }
//
//    @Test
//    @DisplayName("Devrait trouver les garages avec une capacité minimale")
//    public void testFindByCapaciteGreaterThanEqual() {
//        // Given
//        Garage petitGarage = new Garage();
//        petitGarage.setNom("Petit Garage");
//        petitGarage.setCapacite(10);
//
//        Garage grandGarage = new Garage();
//        grandGarage.setNom("Grand Garage");
//        grandGarage.setCapacite(50);
//
//        entityManager.persistAndFlush(petitGarage);
//        entityManager.persistAndFlush(grandGarage);
//
//        // When
//        List<Garage> garagesCapacite = garageRepository.findByCapaciteGreaterThanEqual(20);
//
//        // Then
//        assertThat(garagesCapacite).hasSize(1);
//        assertThat(garagesCapacite.get(0).getNom()).isEqualTo("Grand Garage");
//    }
//
//    @Test
//    @DisplayName("Devrait gérer les exceptions lors de la sauvegarde avec contraintes")
//    public void testSaveWithConstraintViolation() {
//        // Given
//        Garage garage1 = new Garage();
//        garage1.setNom("Garage Unique");
//        garage1.setEmail("unique@garage.fr");
//        entityManager.persistAndFlush(garage1);
//
//        Garage garage2 = new Garage();
//        garage2.setNom("Garage Unique"); // Nom en doublon si contrainte unique
//        garage2.setEmail("unique@garage.fr"); // Email en doublon si contrainte unique
//
//        // When & Then
//        assertThatThrownBy(() -> {
//            garageRepository.save(garage2);
//            entityManager.flush();
//        }).isInstanceOf(DataIntegrityViolationException.class);
//    }
//
//    @Test
//    @DisplayName("Devrait mettre à jour un garage existant")
//    public void testUpdateGarage() {
//        // Given
//        Garage garage = new Garage();
//        garage.setNom("Garage Original");
//        garage.setAdresse("Adresse Originale");
//        garage.setTelephone("01 00 00 00 00");
//        Garage savedGarage = entityManager.persistAndFlush(garage);
//
//        // When
//        savedGarage.setNom("Garage Modifié");
//        savedGarage.setAdresse("Nouvelle Adresse");
//        Garage updatedGarage = garageRepository.save(savedGarage);
//        entityManager.flush();
//
//        // Then
//        assertThat(updatedGarage.getId()).isEqualTo(savedGarage.getId());
//        assertThat(updatedGarage.getNom()).isEqualTo("Garage Modifié");
//        assertThat(updatedGarage.getAdresse()).isEqualTo("Nouvelle Adresse");
//        assertThat(updatedGarage.getTelephone()).isEqualTo("01 00 00 00 00");
//    }
}
