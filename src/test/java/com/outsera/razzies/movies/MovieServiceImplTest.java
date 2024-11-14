package com.outsera.razzies.movies;

import com.outsera.razzies.movie.Movie;
import com.outsera.razzies.movie.MovieRepository;
import com.outsera.razzies.movie.MovieServiceImpl;
import com.outsera.razzies.movie.enumeration.Winner;
import com.outsera.razzies.producer.ProducerAward;
import com.outsera.razzies.util.ResourceFileLoader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@Slf4j
class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieServiceImpl movieService;

    @BeforeEach
    void setUp() {
        // Configuração do mock do repositório
        movieRepository.deleteAll();
        movieRepository.flush();
    }

    @Test
    void testProcessCsv() {
        // Teste garantindo que os dados obtidos estão de acordo com os dados fornecidos na proposta
        // Considerando o arquivo "movielist.csv" disponibilizado junto com as Especificações do Teste do processo de Recrutamento
        InputStream inputStream = null;
        try {
            ResourceFileLoader resourceFileLoader = new ResourceFileLoader("csv/movielist.csv");
            inputStream = resourceFileLoader.getFile();
            // Chamando diretamente o método processCsv
            List<Movie> movies = movieService.processCsv(inputStream);

            // Verificando se a lista não está vazia
            assertThat(movies).isNotEmpty();

            // Exemplo de asserção de que a lista contém o número esperado de filmes (ajuste conforme necessário)
            assertThat(movies).hasSize(206);

            // Verificando um determinado registro
            Movie firstMovie = movies.get(0);
            assertThat(firstMovie.getYear()).isEqualTo(1980);
            assertThat(firstMovie.getTitle()).isEqualTo("Can't Stop the Music");
            assertThat(firstMovie.getWinner()).isEqualTo(Winner.YES);

        } catch (Exception e) {
            fail("Ocorreu um erro ao processar o CSV: " + e.getMessage());
        } finally {
            // Garantindo que o InputStream seja fechado após o uso
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("Erro ao fechar o InputStream: {}", e.getMessage());
                }
            }
        }
    }


    @Test
    void testProducerAwards() {
        // Teste garantindo que o resultado obtido no método processProducerAwards está apresentando um resultado de acordo com os dados fornecidos na proposta
        InputStream inputStream = null;
        try {
            ResourceFileLoader resourceFileLoader = new ResourceFileLoader("csv/movielist.csv");
            inputStream = resourceFileLoader.getFile();
                    // Carregando os registros a partir do CSV
            List<Movie> movies = movieService.processCsv(inputStream);

            // filtar os filmes que ganharam prêmios usando um filtro de programação funcional
            movies = movies.stream().filter(movie -> movie.getWinner().equals(Winner.YES)).toList();

            // Verificando se a lista não está vazia
            assertThat(movies).isNotEmpty();


            // Teste para verificar o resultado do método producerAwards está de acordo com os dados fornecidos na proposta
            // Chamando o método producerAwards
            Map<String, List<ProducerAward>> awards = movieService.processProducerAwards(movies);

            ProducerAward expectedMinAward = ProducerAward.builder()
                    .producer("Joel Silver")
                    .interval(1)
                    .previousWin(1990)
                    .followingWin(1991)
                    .build();

            ProducerAward expectedMaxAward = ProducerAward.builder()
                    .producer("Matthew Vaughn")
                    .interval(13)
                    .previousWin(2002)
                    .followingWin(2015)
                    .build();

            assertThat(awards).isNotNull();
            assertThat(awards).containsKeys("min", "max");

            List<ProducerAward> minAwards = awards.get("min");
            assertThat(minAwards).isNotNull();
            assertThat(minAwards).hasSize(1);
            assertThat(minAwards).containsExactly(expectedMinAward);

            List<ProducerAward> maxAwards = awards.get("max");
            assertThat(maxAwards).isNotNull();
            assertThat(maxAwards).hasSize(1);
            assertThat(maxAwards).containsExactly(expectedMaxAward);


        } catch (Exception e) {
            fail("Ocorreu um erro ao processar o CSV: " + e.getMessage());
        } finally {
            // Garantindo que o InputStream seja fechado após o uso
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("Erro ao fechar o InputStream: {}", e.getMessage());
                }
            }
        }
    }


    private InputStream getFileFromResources(String fileName) {
        // Obtendo o class loader e carregando o arquivo a partir de recursos
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("Arquivo não encontrado: " + fileName);
        }

        return inputStream;
    }

}
