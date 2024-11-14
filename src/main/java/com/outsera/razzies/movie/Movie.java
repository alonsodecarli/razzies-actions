package com.outsera.razzies.movie;

import com.outsera.razzies.movie.enumeration.Winner;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "movies")
@Entity
public class Movie implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "`year`")
    private Integer year;

    private String title;

    private String studios;

    private String producers;

    @Enumerated(EnumType.STRING)
    private Winner winner;

}