package com.reminder.repository;

import com.reminder.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    // Aqui você pode adicionar métodos de consulta personalizados se necessário
}
