package be.vdab.startrek.services;

import be.vdab.startrek.domain.Bestelling;
import be.vdab.startrek.repositories.BestellingRepository;
import be.vdab.startrek.repositories.WerknemerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BestellingService {
    private final BestellingRepository bestellingRepository;
    private final WerknemerRepository werknemerRepository;

    public BestellingService(BestellingRepository bestellingRepository, WerknemerRepository werknemerRepository) {
        this.bestellingRepository = bestellingRepository;
        this.werknemerRepository = werknemerRepository;
    }

    public List<Bestelling> findAll(long werknemerId) {
        return bestellingRepository.findAll(werknemerId);
    }

    @Transactional
    public void bestel(Bestelling bestelling) {
            werknemerRepository.lowerBudget(bestelling.getWerknemerId(), bestelling.getBedrag());
            bestellingRepository.create(bestelling);
    }
}
