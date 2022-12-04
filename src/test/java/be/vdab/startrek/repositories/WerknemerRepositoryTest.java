package be.vdab.startrek.repositories;

import be.vdab.startrek.domain.Werknemer;
import be.vdab.startrek.exceptions.BudgetTooLowException;
import be.vdab.startrek.exceptions.WerknemerNietGevondenException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@JdbcTest
@Import(WerknemerRepository.class)
@Sql("/insertWerknemers.sql")
class WerknemerRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static final String WERKNEMERS = "werknemers";
    private final WerknemerRepository repository;

    public WerknemerRepositoryTest(WerknemerRepository repository) {
        this.repository = repository;
    }

    @Test
    void findAllGeeftAlleWerknemersGesorteerdOpVoornaam() {
        assertThat(repository.findAll())
                .hasSize(countRowsInTable(WERKNEMERS))
                .extracting(Werknemer::getVoornaam)
                .isSortedAccordingTo(String::compareToIgnoreCase);
    }

    private long idVanTestWerknemer() {
        return jdbcTemplate
                .queryForObject("select id from werknemers where voornaam = 'testSven'", Long.class);
    }

    private long idVanTestWerknemer2() {
        return jdbcTemplate
                .queryForObject("select id from werknemers where voornaam = 'testHarrie'", Long.class);
    }


    @Test
    void findById() {
        assertThat(repository.findById(idVanTestWerknemer()))
                .hasValueSatisfying(werknemer->assertThat(werknemer.getVoornaam()).isEqualTo("testSven"));
    }

    @Test
    void findByOnbestaandeIdVindtGeenWerknemer() {
        assertThat(repository.findById(-1)).isEmpty();
    }

    @Test
    void lowerBudget() {
        repository.lowerBudget(idVanTestWerknemer2(), BigDecimal.TEN);
        assertThat(repository.findById(idVanTestWerknemer2()).get().getBudget())
                .isEqualByComparingTo(BigDecimal.valueOf(9989.99));
    }

    @Test
    void lowerBudgetWordtNietNegatiefEnGeeftFout() {
        assertThatExceptionOfType(BudgetTooLowException.class).isThrownBy(
                ()->repository.lowerBudget(idVanTestWerknemer2(), BigDecimal.valueOf(10000))
        );
        assertThat(repository.findById(idVanTestWerknemer2()).get().getBudget())
                .isEqualByComparingTo(BigDecimal.valueOf(9999.99));
    }

    @Test
    void lowerBudgetVanOnbestaandeWerknemerGeeftFout() {
        assertThatExceptionOfType(WerknemerNietGevondenException.class).isThrownBy(
                ()->repository.lowerBudget(-1, BigDecimal.ONE)
        );
    }
}
