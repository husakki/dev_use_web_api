package org.tzi.use.use_logic.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.tzi.use.use_logic.entities.ModelNTT;

import java.util.Optional;

public interface ModelRepo extends MongoRepository<ModelNTT, String> {
    Optional<ModelNTT> findByClassesName(String className);
}
