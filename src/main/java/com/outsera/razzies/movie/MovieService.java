package com.outsera.razzies.movie;

import com.outsera.razzies.producer.ProducerAward;

import java.util.List;
import java.util.Map;

public interface MovieService {

    public List<Movie> loadFromCsv();
    public Map<String, List<ProducerAward>> producerAwards();
}
