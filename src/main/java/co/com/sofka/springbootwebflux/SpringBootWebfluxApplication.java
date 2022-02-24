package co.com.sofka.springbootwebflux;

import co.com.sofka.springbootwebflux.models.Categoria;
import co.com.sofka.springbootwebflux.models.Producto;
import co.com.sofka.springbootwebflux.services.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Date;

@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebfluxApplication.class, args);
    }

    @Autowired
    private ProductoService service;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApplication.class);


    @Override
    public void run(String... args) throws Exception {

        mongoTemplate.dropCollection("productos")
                .subscribe();
        mongoTemplate.dropCollection("categorias")
                .subscribe();

        Categoria electronico = new Categoria("Electronico");
        Categoria deporte = new Categoria("Deporte");
        Categoria computacion = new Categoria("Computacion");
        Categoria muebles = new Categoria("Muebles");

        Flux.just(electronico,deporte,computacion,muebles)
                        .flatMap(categoria -> service.saveCatecoria(categoria))
                        .doOnNext(c -> {
                            log.info("Categoria creada: "+ c.getNombre() + ", Id: " + c.getId());
                        }).thenMany(Flux.just(
                                new Producto("Pantalla de 24'", 45.50, electronico),
                                new Producto("Teclado", 5.50, computacion),
                                new Producto("Mouse", 7.80, computacion),
                                new Producto("Impresora", 23.40, computacion),
                                new Producto("Smart Phone", 68.35, electronico),
                                new Producto("Audifonos", 5.50, electronico),
                                new Producto("Microfono", 17.60, electronico),
                                new Producto("Camara", 62.35, electronico),
                                new Producto("Air Force One", 45.50, deporte),
                                new Producto("Gucci", 3000.99, deporte)
                        )
                        .flatMap(producto -> {
                            producto.setCreateAt(new Date());
                            return service.save(producto);
                        }))
                        .subscribe(producto -> log.info("Insert: " + producto.getId() + " " + producto.getNombre()));
    }
}
