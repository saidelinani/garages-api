package com.renault.garagesapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Garage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String telephone;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JourHoraire> horairesOuverture = new ArrayList<>();

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehicule> vehicules = new ArrayList<>();
}
