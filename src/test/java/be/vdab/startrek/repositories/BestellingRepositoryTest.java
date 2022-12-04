package be.vdab.startrek.repositories;

import be.vdab.startrek.domain.Bestelling;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(BestellingRepository.class)
@Sql("/insertBestellingen.sql")
class BestellingRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static final String BESTELLINGEN = "bestellingen";
    private final BestellingRepository repository;

    BestellingRepositoryTest(BestellingRepository repository) {
        this.repository = repository;
    }

    @Test
    void findAllGeeftAlleBestellingenGesorteerdOpId() {
        assertThat(repository.findAll(9998L))
                .hasSize(countRowsInTableWhere(BESTELLINGEN, "werknemerId = 9998"))
                .allSatisfy(bestelling ->
                        assertThat(bestelling.getWerknemerId()).isEqualByComparingTo(9998L))
                .extracting(Bestelling::getId)
                .isSorted();
    }

    @Test
    void findAllGeeftLegeVerzamelingBestellingenBijOnbestaandeWerknemerId() {
        assertThat(repository.findAll(-1)).isEmpty();
    }

    @Test
    void create() {
        var id = repository.create(new Bestelling(0, 1, "tester", BigDecimal.TEN));
        assertThat(id).isPositive();
        assertThat(countRowsInTableWhere(BESTELLINGEN, "id = " + id)).isOne();
    }
}
