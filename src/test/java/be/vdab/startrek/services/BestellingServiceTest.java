package be.vdab.startrek.services;

import be.vdab.startrek.domain.Bestelling;
import be.vdab.startrek.repositories.BestellingRepository;
import be.vdab.startrek.repositories.WerknemerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BestellingServiceTest {
    @Mock
    private BestellingRepository bestellingRepository;
    @Mock
    private WerknemerRepository werknemerRepository;
    private BestellingService service;
    @BeforeEach
    void beforeEach() {
        service = new BestellingService(bestellingRepository, werknemerRepository);
    }

    @Test
    void bestel() {
        var bestelling = new Bestelling(0, 1, "test", BigDecimal.TEN);
        service.bestel(bestelling);
        verify(werknemerRepository).lowerBudget(1, BigDecimal.TEN);
        verify(bestellingRepository).create(bestelling);
    }
}
