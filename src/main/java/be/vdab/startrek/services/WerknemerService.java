package be.vdab.startrek.services;

import be.vdab.startrek.domain.Werknemer;
import be.vdab.startrek.repositories.WerknemerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class WerknemerService {
    private final WerknemerRepository repository;

    public WerknemerService(WerknemerRepository repository) {
        this.repository = repository;
    }

    public List<Werknemer> findAll() {
        return repository.findAll();
    }
    public Optional<Werknemer> findById(long id) {
        return repository.findById(id);
    }
}


