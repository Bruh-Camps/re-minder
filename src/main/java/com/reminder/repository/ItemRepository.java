package com.reminder.repository;

import com.reminder.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByUserId(Long userId);

    // Aqui você pode adicionar métodos de consulta personalizados se necessário
}
