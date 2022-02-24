package co.com.sofka.springbootwebflux.dao;

import co.com.sofka.springbootwebflux.models.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductoDao extends ReactiveMongoRepository<Producto, String> {

}
