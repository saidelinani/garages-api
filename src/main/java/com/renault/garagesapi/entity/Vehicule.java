package com.renault.garagesapi.entity;

import com.renault.garagesapi.enums.TypeCarburant;
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
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private int anneeFabrication;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeCarburant typeCarburant;

    @ManyToOne
    @JoinColumn(name = "garage_id")
    private Garage garage;

    @OneToMany(mappedBy = "vehicule", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Accessoire> accessoires = new ArrayList<>();

}
