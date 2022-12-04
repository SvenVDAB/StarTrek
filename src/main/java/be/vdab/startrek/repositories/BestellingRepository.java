package be.vdab.startrek.repositories;

import be.vdab.startrek.domain.Bestelling;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BestellingRepository {
    private final JdbcTemplate template;
    private final SimpleJdbcInsert insert;

    public BestellingRepository(JdbcTemplate template) {
        this.template = template;
        insert = new SimpleJdbcInsert(template)
                .withTableName("bestellingen")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<Bestelling> bestellingMapper =
            (result, rowNum) -> new Bestelling(result.getLong("id"),
                    result.getLong("werknemerId"),
                    result.getString("omschrijving"),
                    result.getBigDecimal("bedrag"));

    public List<Bestelling> findAll(long werknemerId) {
        var sql = """
                select id, werknemerId, omschrijving, bedrag
                from bestellingen
                where werknemerId = ?
                order by id
                """;
        return template.query(sql, bestellingMapper, werknemerId);
    }

    public long create(Bestelling bestelling) {
        return insert.executeAndReturnKey(
                Map.of("werknemerId", bestelling.getWerknemerId(),
                        "omschrijving", bestelling.getOmschrijving(),
                        "bedrag", bestelling.getBedrag()))
                .longValue();
    }
}
