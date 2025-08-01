package com.renault.garagesapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JourHoraire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "jour", nullable = false)
    private DayOfWeek jour;

    @ManyToOne
    @JoinColumn(name = "garage_id", nullable = false)
    private Garage garage;

    @ElementCollection
    @CollectionTable(
            name = "jour_creneaux",
            joinColumns = @JoinColumn(name = "jour_id")
    )
    @OrderBy("startTime ASC")
    private List<OpeningTime> creneaux = new ArrayList<>();
}
