package co.com.sofka.springbootwebflux.dao;

import co.com.sofka.springbootwebflux.models.Categoria;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoriaDao extends ReactiveMongoRepository<Categoria, String> {
}
