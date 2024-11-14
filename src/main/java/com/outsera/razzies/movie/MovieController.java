package com.outsera.razzies.movie;

import com.outsera.razzies.movie.enumeration.Winner;
import com.outsera.razzies.producer.ProducerAward;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieRepository movieRepository;
    private final MovieService movieService;

    @Operation(
            summary = "Exibe um filme específico",
            description = "Esse endpoint retorna os detalhes de um filme com base no ID fornecido. Caso o filme não seja encontrado, um erro 404 será retornado.",
            tags = { "Movies" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filme encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @GetMapping("{id}")
    public ResponseEntity<Movie> show(@PathVariable Long id) {
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isPresent()) {
            return ResponseEntity.ok(movie.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @Operation(
            summary = "Armazena um filme",
            description = "Esse endpoint cria um novo filme. Em caso de sucesso, retorna o filme criado. Caso haja algum erro de validação, um erro apropriado é retornado.",
            tags = { "Movies" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Filme criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "409", description = "Conflito: recurso já existe"),
            @ApiResponse(responseCode = "422", description = "Entidade não processável"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PostMapping
    public ResponseEntity<Movie> save(@RequestBody Movie movie) {
        Movie savedMovie = movieRepository.save(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }

    @Operation(
            summary = "Atualiza um filme existente",
            description = "Esse endpoint permite atualizar um filme existente com base no ID fornecido. Caso o filme não seja encontrado, retorna um erro 404. Se o filme for atualizado com sucesso, retorna o filme atualizado.",
            tags = { "Movies" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filme atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PutMapping("{id}")
    public ResponseEntity<Movie> update(@PathVariable Long id, @RequestBody Movie movie) {
        Optional<Movie> existingMovie = movieRepository.findById(id);
        if (existingMovie.isPresent()) {
            movie.setId(id);
            Movie updatedMovie = movieRepository.save(movie);
            movieRepository.flush();
            return ResponseEntity.ok(updatedMovie);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(
            summary = "Deleta um filme específico",
            description = "Este endpoint permite a exclusão de um filme específico com base no ID fornecido. Se o filme for encontrado e deletado com sucesso, retorna uma resposta 204 (Sem Conteúdo). Caso o filme não seja encontrado, retorna uma resposta 404 (Não Encontrado).",
            tags = { "Movies" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Filme deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isPresent()) {
            movieRepository.delete(movie.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @Operation(
            summary = "Lista todos os filmes",
            description = "Esse endpoint retorna a lista de todos os filmes registrados. Caso não haja filmes cadastrados, retorna uma resposta 204 (sem conteúdo).",
            tags = { "Movies" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de filmes retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum filme encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @GetMapping
    public ResponseEntity<List<Movie>> findAll() {
        List<Movie> movies = movieRepository.findAll();
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @Operation(
            summary = "Lista todos os filmes vencedores",
            description = "Esse endpoint retorna a lista de todos os filmes que venceram prêmios. Caso não haja filmes vencedores cadastrados, retorna uma resposta 204 (sem conteúdo).",
            tags = { "Movies" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de filmes vencedores retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum filme vencedor encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @GetMapping("/winners")
    public ResponseEntity<List<Movie>> findWinners() {
        List<Movie> movies = movieRepository.findByWinner(Winner.YES);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }


    @Operation(
            summary = "Lista todos os filmes vencedores de um ano específico",
            description = "Esse endpoint retorna a lista de todos os filmes que venceram prêmios em um ano específico. Caso não haja filmes vencedores cadastrados para o ano fornecido, retorna uma resposta 204 (sem conteúdo).",
            tags = { "Movies" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de filmes vencedores retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum filme vencedor encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @GetMapping("/winners/{year}")
    public ResponseEntity<List<Movie>> findWinnersByYear(@PathVariable Integer year) {
        List<Movie> movies = movieRepository.findByWinnerAndYear(Winner.YES, year);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }


    @Operation(
            summary = "Lista todos os filmes não vencedores",
            description = "Esse endpoint retorna a lista de todos os filmes que não venceram prêmios. Caso não haja filmes não vencedores cadastrados, retorna uma resposta 204 (sem conteúdo).",
            tags = { "Movies" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de filmes não vencedores retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum filme não vencedor encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @GetMapping("/non-winners")
    public ResponseEntity<List<Movie>> findNonWinners() {
        List<Movie> movies = movieRepository.findByWinner(Winner.NO);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }


    @Operation(
            summary = "Lista todos os filmes não vencedores de um ano específico",
            description = "Esse endpoint retorna a lista de todos os filmes que não venceram prêmios em um ano específico. Caso não haja filmes não vencedores cadastrados para o ano fornecido, retorna uma resposta 204 (sem conteúdo).",
            tags = { "Movies" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de filmes não vencedores retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum filme não vencedor encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @GetMapping("/non-winners/{year}")
    public ResponseEntity<List<Movie>> findNonWinnersByYear(@PathVariable Integer year) {
        List<Movie> movies = movieRepository.findByWinnerAndYear(Winner.NO, year);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }


    @Operation(
            summary = "Listando os produtores com maior e menor intervalo de prêmios",
            description = "Esse endpoint retorna o produtor com maior intervalo entre dois prêmios consecutivos, e o que\n" +
                    "obteve dois prêmios mais rápido, seguindo a especificação de formato definida na proposta do Teste de Recrutamento.",
            tags = { "Producer Awards" }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Movies with awards retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = Map.class,
                                    description = "Retorna um mapa de produtores com listas de prêmios"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    type = "string",
                                    description = "Mensagem de erro caso ocorra falha no servidor"
                            )
                    )
            )
    })
    @GetMapping("/producer-awards")
    public Map<String, List<ProducerAward>> producerAwards() {
        return movieService.producerAwards();
    }


}
