package com.outsera.razzies.movie;

import com.outsera.razzies.movie.enumeration.Winner;
import com.outsera.razzies.producer.ProducerAward;
import com.outsera.razzies.util.ResourceFileLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    // Valor padrão do caminho do CSV, configurado no application.properties ou application.yaml
    @Value("${csv.filepath}")
    private String defaultCsvFilePath;

    private final MovieRepository movieRepository;

    // Função para carregar os filmes do arquivo CSV
    @Override
    public List<Movie> loadFromCsv() {
        log.info("Carregando filmes do arquivo CSV.");
        List<Movie> movies = new ArrayList<>();

        ResourceFileLoader resourceFileLoader = new ResourceFileLoader(defaultCsvFilePath);
        try (InputStream inputStream = resourceFileLoader.getFile()) {
            movies = processCsv(inputStream);
        } catch (IOException e) {
            log.error("Erro ao ler o arquivo CSV", e);
            throw new RuntimeException("Erro ao ler o arquivo CSV", e);
        }

        return movies;
    }

    public List<Movie> processCsv(InputStream inputStream) {
        List<Movie> movies = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            // Ignora o cabeçalho e mapeia as linhas para objetos Movie
            movies = reader.lines().skip(1) // Pula o cabeçalho
                    .map(this::processCsvLine)  // Mapeia cada linha para um objeto Movie
                    .filter(Objects::nonNull)  // Filtra linhas inválidas
                    .collect(Collectors.toList());  // Coleta todos os filmes

            // Salva todos os filmes lidos no banco de dados
            movieRepository.saveAll(movies);
            movieRepository.flush();
            log.info("Total de filmes armazenados com sucesso: {}", movies.size());
        } catch (IOException e) {
            log.error("Erro ao processar o arquivo CSV", e);
            throw new RuntimeException("Erro ao processar o arquivo CSV", e);
        }

        return movies;
    }

    private Movie processCsvLine(String line) {
        String[] fields = line.split(";");

        if (fields.length < 4) {
            log.warn("Registro/Linha inválida (menos de 4 campos): {}", line);
            return null; // Ignora linhas inválidas
        }

        try {
            // Verifica se o ano pode ser convertido para Integer
            Integer year = parseYear(fields[0]);
            if (year == null) {
                log.warn("Ano inválido para a linha: {}", line);
                return null; // Ignora a linha caso o ano seja inválido
            }

            // Cria o objeto Movie a partir dos campos usando o padrão de projeto Builder
            return Movie.builder()
                    .year(year)
                    .title(fields[1])
                    .studios(fields[2])
                    .producers(fields[3])
                    .winner(Winner.fromString(fields.length > 4 ? fields[4] : "no")) // Default para "no" se não houver vencedor
                    .build();
        } catch (Exception e) {
            log.error("Erro ao processar a linha (dados mal formatados): {}", line, e);
            return null;
        }
    }

    private Integer parseYear(String yearStr) {
        try {
            return Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            return null;
        }
    }


    public Map<String, List<ProducerAward>> producerAwards() {
        log.info("Calculando intervalos de prêmios para produtores...");

        List<Movie> movies = getAwardsMovies();

        return processProducerAwards(movies);
    }

    public static Map<String, List<ProducerAward>> processProducerAwards(List<Movie> movies) {
        // Agrupar filmes por produtores
        Map<String, List<Movie>> moviesByProducer = movies.stream()
                .flatMap(movie -> Arrays.stream(movie.getProducers().split(",|\\band\\b"))  // Divida por vírgula ou "and"
                        .map(producer -> producer.trim()) // Remover espaços antes e depois do nome do produtor
                        .filter(producer -> !producer.isEmpty())  // Filtrar para remover produtores vazios
                        .map(producer -> new AbstractMap.SimpleEntry<>(producer, movie))) // Criar um par produtor -> filme
                .collect(Collectors.groupingBy(Map.Entry::getKey, // Agrupar pelo produtor
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));  // Criar lista de filmes para cada produtor

        List<ProducerAward> minIntervalAwards = new ArrayList<>();
        List<ProducerAward> maxIntervalAwards = new ArrayList<>();
        long globalMinInterval = Long.MAX_VALUE;
        long globalMaxInterval = 0;

        // Para cada produtor, calcular os intervalos entre os anos de premiação
        for (Map.Entry<String, List<Movie>> entry : moviesByProducer.entrySet()) {
            String producer = entry.getKey();
            List<Movie> producerMovies = entry.getValue();

            // Ignorar produtores com apenas um prêmio (não há intervalo para calcular)
            if (producerMovies.size() < 2) {
                log.info("Produtor {} tem apenas um prêmio, ignorando.", producer);
                continue;
            }

            // Ordenar os filmes por ano
            producerMovies.sort(Comparator.comparing(Movie::getYear));

            // Calcular os intervalos entre os prêmios
            for (int i = 1; i < producerMovies.size(); i++) {
                long interval = producerMovies.get(i).getYear() - producerMovies.get(i - 1).getYear();
                int previousWin = producerMovies.get(i - 1).getYear();
                int followingWin = producerMovies.get(i).getYear();

                // Atualizar lista de menores intervalos
                if (interval < globalMinInterval) {
                    globalMinInterval = interval;
                    minIntervalAwards.clear(); // Limpar lista para adicionar novos mínimos
                    minIntervalAwards.add(ProducerAward.builder()
                            .producer(producer)
                            .interval(interval)
                            .previousWin(previousWin)
                            .followingWin(followingWin)
                            .build());
                } else if (interval == globalMinInterval) {
                    minIntervalAwards.add(ProducerAward.builder()
                            .producer(producer)
                            .interval(interval)
                            .previousWin(previousWin)
                            .followingWin(followingWin)
                            .build());
                }


                // Atualizar lista de maiores intervalos
                if (interval > globalMaxInterval) {
                    globalMaxInterval = interval;
                    maxIntervalAwards.clear(); // Limpar lista para adicionar novos máximos
                    maxIntervalAwards.add(ProducerAward.builder()
                            .producer(producer)
                            .interval(interval)
                            .previousWin(previousWin)
                            .followingWin(followingWin)
                            .build());
                } else if (interval == globalMaxInterval) {
                    maxIntervalAwards.add(ProducerAward.builder()
                            .producer(producer)
                            .interval(interval)
                            .previousWin(previousWin)
                            .followingWin(followingWin)
                            .build());
                }
            }
        }

        showAwards(moviesByProducer);

        // Montar o resultado final com os menores e maiores intervalos
        Map<String, List<ProducerAward>> result = new HashMap<>();
        result.put("min", minIntervalAwards);
        result.put("max", maxIntervalAwards);

        log.info("Intervalos de prêmios calculados com sucesso.");
        return result;
    }

    public List<Movie> getAwardsMovies() {
        // Busca todos os filmes vencedores
        List<Movie> movies = movieRepository.findByWinner(Winner.YES);
        return movies;
    }

    private static void showAwards(Map<String, List<Movie>> moviesByProducer) {
        // exibir a relação de produtores premiados e os respectivos intervalos
        log.info("Relação de produtores com mais de um prêmio:");
        for (Map.Entry<String, List<Movie>> entry : moviesByProducer.entrySet()) {
            String producer = entry.getKey();
            List<Movie> producerMovies = entry.getValue();
            if (producerMovies.size() >= 2) {
                log.info("Produtor: {}", producer);
                for (Movie movie : producerMovies) {
                    log.info("  - {} ({})", movie.getTitle(), movie.getYear());
                }
            }
        }
    }

}