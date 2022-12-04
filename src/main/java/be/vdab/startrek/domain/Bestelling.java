package be.vdab.startrek.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;

public class Bestelling {
    private final long id;
    private final long werknemerId;

    @NotBlank
    private final String omschrijving;
    @NotNull
    @Positive
    @NumberFormat(pattern="#,##0.00")
    private final BigDecimal bedrag;

    public Bestelling(long id, long werknemerId, String omschrijving, BigDecimal bedrag) {
        this.id = id;
        this.werknemerId = werknemerId;
        this.omschrijving = omschrijving;
        this.bedrag = bedrag;
    }

    public long getId() {
        return id;
    }

    public long getWerknemerId() {
        return werknemerId;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public BigDecimal getBedrag() {
        return bedrag;
    }
}
