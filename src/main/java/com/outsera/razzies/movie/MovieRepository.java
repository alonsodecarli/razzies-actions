package com.outsera.razzies.movie;

import com.outsera.razzies.movie.enumeration.Winner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByWinner(Winner winner);
    List<Movie> findByWinnerAndYear(Winner winner, Integer year);

}
