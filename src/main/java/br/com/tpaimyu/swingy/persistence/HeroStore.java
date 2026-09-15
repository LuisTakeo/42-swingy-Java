package br.com.tpaimyu.swingy.persistence;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import br.com.tpaimyu.swingy.models.Hero;

public interface HeroStore {
    void save(Hero hero) throws IOException;
    List<Hero> loadAll() throws IOException;
    Optional<Hero> load(int index) throws IOException;

    default Optional<Hero> load() throws IOException {
        return load(0);
    }
}
