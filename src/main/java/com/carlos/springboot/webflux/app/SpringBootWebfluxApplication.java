package com.carlos.springboot.webflux.app;

import com.carlos.springboot.webflux.app.models.documents.Categoria;
import com.carlos.springboot.webflux.app.models.documents.Producto;
import com.carlos.springboot.webflux.app.models.services.ProductoService;
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

	@Autowired
	private ProductoService service;

	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		mongoTemplate.dropCollection("productos").subscribe();
		mongoTemplate.dropCollection("categorias").subscribe();

		Categoria electrónico = new Categoria("Electrónico");
		Categoria deporte = new Categoria("Deporte");
		Categoria computación = new Categoria("Computación");
		Categoria muebles = new Categoria("Muebles");

		Flux.just(electrónico, deporte, computación, muebles)
				.flatMap(service::saveCategoria)
				.doOnNext(categoria -> log.info("Categoria creada: " + categoria.getNombre() + "Id: " + categoria.getId()))
				.thenMany(
						Flux.just(new Producto("TV Panasonic Pantalla LCD", 490.990, electrónico),
							new Producto("Sony Camara HD Digital", 177.89, electrónico),
							new Producto("Apple iPod", 46.89, electrónico),
							new Producto("Sony Notebook", 846.89, computación),
							new Producto("Bianchi Bicicleta", 70.89, deporte),
							new Producto("Mica Cómoda 5 cajones", 150.89, muebles),
							new Producto("HP NoteBook Omen 17'", 900.89, computación))
						.flatMap(producto -> {
							producto.setCreateAt(new Date());
							return service.save(producto);
						}))
				.subscribe(producto -> log.info("insert: " + producto.getId() + " " + producto.getNombre()));
	}
}
