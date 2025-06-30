
/*package com.cinerama.backend.service;

import com.cinerama.backend.entity.CartItem;
import com.cinerama.backend.entity.Order;
import com.cinerama.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, MovieRepository movieRepository) {
        this.orderRepository = orderRepository;
        this.movieRepository = movieRepository;
    }

    public Order save(Order order) {
        if (order.getCart() != null) {
            for (CartItem item : order.getCart()) {
                // Verificamos que la película exista
                if (!movieRepository.existsById(item.getMovieId())) {
                    throw new IllegalArgumentException("La película con ID " + item.getMovieId() + " no existe.");
                }

                if (item.getPrice() == 0) {
                    Movie movie = movieRepository.findById(item.getMovieId())
                            .orElseThrow(() -> new IllegalArgumentException("La película con ID " + item.getMovieId() + " no existe."));

                    if ("Infantil".equalsIgnoreCase(movie.getGenre())) {
                        item.setPrice(10);
                    } else {
                        item.setPrice(15);
                    }
                }

                item.setOrder(order);
            }
        }
        return orderRepository.save(order);
    }
}
 */