package be.vdab.startrek.repositories;

import be.vdab.startrek.domain.Werknemer;
import be.vdab.startrek.exceptions.BudgetTooLowException;
import be.vdab.startrek.exceptions.WerknemerNietGevondenException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class WerknemerRepository {
    private final JdbcTemplate template;

    public WerknemerRepository(JdbcTemplate template) {
        this.template = template;
    }

    private final RowMapper<Werknemer> werknemerMapper =
            (result, rowNum) -> new Werknemer(result.getLong("id"), result.getString("voornaam"),
                    result.getString("familienaam"), result.getBigDecimal("budget"));

    public List<Werknemer> findAll() {
        var sql = """
                select id, voornaam, familienaam, budget
                from werknemers
                order by voornaam, familienaam
                """;
        return template.query(sql, werknemerMapper);
    }

    public Optional<Werknemer> findById(long id) {
        try {
            var sql = """
                    select id, voornaam, familienaam, budget
                    from werknemers 
                    where id = ?
                    """;
            return Optional.of(template.queryForObject(sql, werknemerMapper, id));
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }

    public void lowerBudget(long id, BigDecimal bedrag) {
        var sql = """
                update werknemers
                set budget = budget - ?
                where id = ? and budget >= ?
                """;
        var nrChangedRecords = template.update(sql, bedrag, id, bedrag);
        if (nrChangedRecords == 0) {
            if (findById(id).isEmpty()) {
                throw new WerknemerNietGevondenException();
            } else {
                throw new BudgetTooLowException();
            }
        }
    }
}

