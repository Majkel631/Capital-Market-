package org.zadanko.persistence;

import org.zadanko.logic.Portfolio;

import java.nio.file.Path;

public interface PortfolioFileRepository {

    void save(Portfolio portfolio, Path path);

    Portfolio load(Path path);
}