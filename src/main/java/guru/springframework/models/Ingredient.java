package guru.springframework.models;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;


@Getter
@Setter
@EqualsAndHashCode(exclude = {"recipe"})
//@EqualsAndHashCode(callSuper = false)
@Entity
public class Ingredient extends BaseEntity{

    private String description;
    private BigDecimal amount;

    @OneToOne(fetch = FetchType.EAGER) // Retrieve this field every time from the DB
    private UnitOfMeasure uom;

    // Map: Many Ingredient -> One Recipe , While the Recipe owns the Ingredient entity
    // No Cascade here, Deleting an Ingredient should not delete a Recipe
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    public Ingredient() {
    }

    public Ingredient(String description, BigDecimal amount, UnitOfMeasure uom) {
        this.description = description;
        this.amount = amount;
        this.uom = uom;
    }

}
