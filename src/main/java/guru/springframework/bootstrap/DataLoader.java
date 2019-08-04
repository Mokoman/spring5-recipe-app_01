package guru.springframework.bootstrap;

import guru.springframework.domain.*;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final static String GUACAMOLE_DIRECTIONS =
            "1 Cut avocado, remove flesh: Cut the avocados in half. Remove seed. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. (See How to Cut and Peel an Avocado.) Place in a bowl.\n" +
                    "\n" +
                    "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)\n" +
                    "\n" +
                    "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n" +
                    "\n" +
                    "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n" +
                    "\n" +
                    "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n" +
                    "\n" +
                    "4 Cover with plastic and chill to store: Place plastic wrap on the surface of the guacamole cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.\n" +
                    "\n" +
                    "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.";

    private final static String GUACAMOLE_NOTES =
            "For a very quick guacamole just take a 1/4 cup of salsa and mix it in with your mashed avocados.\n" +
                    "\n" +
                    "Feel free to experiment! One classic Mexican guacamole has pomegranate seeds and chunks of peaches in it (a Diana Kennedy favorite). Try guacamole with added pineapple, mango, or strawberries (see our Strawberry Guacamole).\n" +
                    "\n" +
                    "The simplest version of guacamole is just mashed avocados with salt. Don't let the lack of availability of other ingredients stop you from making guacamole.\n" +
                    "\n" +
                    "To extend a limited supply of avocados, add either sour cream or cottage cheese to your guacamole dip. Purists may be horrified, but so what? It tastes great.\n" +
                    "\n" +
                    "For a deviled egg version with guacamole, try our Guacamole Deviled Eggs!";

    private final static String SPICY_GRILLED_CHICKEN_TACO_DIRECTIONS =
                    "1 Prepare a gas or charcoal grill for medium-high, direct heat.\n" +
                    "\n" +
                    "2 Make the marinade and coat the chicken: In a large bowl, stir together the chili powder, oregano, cumin, sugar, salt, garlic and orange zest. Stir in the orange juice and olive oil to make a loose paste. Add the chicken to the bowl and toss to coat all over.\n" +
                    "\n" +
                    "Set aside to marinate while the grill heats and you prepare the rest of the toppings.\n" +
                    "\n" +
                    "Spicy Grilled Chicken Tacos\n" +
                    "\n" +
                    "3 Grill the chicken: Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted into the thickest part of the meat registers 165F. Transfer to a plate and rest for 5 minutes.\n" +
                    "\n" +
                    "4 Warm the tortillas: Place each tortilla on the grill or on a hot, dry skillet over medium-high heat. As soon as you see pockets of the air start to puff up in the tortilla, turn it with tongs and heat for a few seconds on the other side.\n" +
                    "\n" +
                    "Wrap warmed tortillas in a tea towel to keep them warm until serving.\n" +
                    "\n" +
                    "5 Assemble the tacos: Slice the chicken into strips. On each tortilla, place a small handful of arugula. Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned sour cream. Serve with lime wedges.";

    private final static String SPICY_GRILLED_CHICKEN_TACO_NOTES =
            "We have a family motto and it is this: Everything goes better in a tortilla.\n" +
                    "\n" +
                    "Any and every kind of leftover can go inside a warm tortilla, usually with a healthy dose of pickled jalapenos. I can always sniff out a late-night snacker when the aroma of tortillas heating in a hot pan on the stove comes wafting through the house.\n" +
                    "\n" +
                    "Today’s tacos are more purposeful – a deliberate meal instead of a secretive midnight snack!\n" +
                    "\n" +
                    "First, I marinate the chicken briefly in a spicy paste of ancho chile powder, oregano, cumin, and sweet orange juice while the grill is heating. You can also use this time to prepare the taco toppings.\n" +
                    "\n" +
                    "Grill the chicken, then let it rest while you warm the tortillas. Now you are ready to assemble the tacos and dig in. The whole meal comes together in about 30 minutes!\n" +
                    "\n" +
                    "Spicy Grilled Chicken TacosThe ancho chiles I use in the marinade are named for their wide shape. They are large, have a deep reddish brown color when dried, and are mild in flavor with just a hint of heat. You can find ancho chile powder at any markets that sell Mexican ingredients, or online.\n" +
                    "\n" +
                    "I like to put all the toppings in little bowls on a big platter at the center of the table: avocados, radishes, tomatoes, red onions, wedges of lime, and a sour cream sauce. I add arugula, as well – this green isn’t traditional for tacos, but we always seem to have some in the fridge and I think it adds a nice green crunch to the tacos.\n" +
                    "\n" +
                    "Everyone can grab a warm tortilla from the pile and make their own tacos just they way they like them.\n" +
                    "\n" +
                    "You could also easily double or even triple this recipe for a larger party. A taco and a cold beer on a warm day? Now that’s living!";

    private RecipeRepository recipeRepository;
    private CategoryRepository categoryRepository;
    private UnitOfMeasureRepository unitOfMeasureRepository;

    public DataLoader(RecipeRepository recipeRepository, CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.debug("Loading Bootstrap data...");
        recipeRepository.saveAll(getRecipes());
    }

    private List<Recipe> getRecipes(){
        List<Recipe> recipes = new ArrayList<>(2);

        //get UOMs
        Optional<UnitOfMeasure> eachUOMOptional = unitOfMeasureRepository.findByUom("Each");

        if(!eachUOMOptional.isPresent()){
            new RuntimeException("Expected UOM Not Found!");
        }

        Optional<UnitOfMeasure> tableSpoonUOMOptional = unitOfMeasureRepository.findByUom("Tablespoon");

        if(!tableSpoonUOMOptional.isPresent()){
            new RuntimeException("Expected UOM Not Found!");
        }

        Optional<UnitOfMeasure> teaSpoonUOMOptional = unitOfMeasureRepository.findByUom("Teaspoon");

        if(!teaSpoonUOMOptional.isPresent()){
            new RuntimeException("Expected UOM Not Found!");
        }

        Optional<UnitOfMeasure> dashSpoonUOMOptional = unitOfMeasureRepository.findByUom("Dash");

        if(!dashSpoonUOMOptional.isPresent()){
            new RuntimeException("Expected UOM Not Found!");
        }

        Optional<UnitOfMeasure> pintUOMOptional = unitOfMeasureRepository.findByUom("Pint");

        if(!pintUOMOptional.isPresent()){
            new RuntimeException("Expected UOM Not Found!");
        }

        Optional<UnitOfMeasure> cupsUOMOptional = unitOfMeasureRepository.findByUom("Cup");

        if(!cupsUOMOptional.isPresent()){
            new RuntimeException("Expected UOM Not Found!");
        }

        // get optionals
        UnitOfMeasure eachUOM = eachUOMOptional.get();
        UnitOfMeasure tableSpoonUOM = tableSpoonUOMOptional.get();
        UnitOfMeasure teaSpoonUOM = teaSpoonUOMOptional.get();
        UnitOfMeasure dashSpoonUOM = dashSpoonUOMOptional.get();
        UnitOfMeasure pintUOM = pintUOMOptional.get();
        UnitOfMeasure cupsUOM = cupsUOMOptional.get();

        //get Categories
        Optional<Category> americanCategoryOptional = categoryRepository.findByDescription("American");

        if(!americanCategoryOptional.isPresent()){
            throw new RuntimeException("Expected Category Not Found!");
        }

        Category americanCategory = americanCategoryOptional.get();

        Optional<Category> mexicanCategoryOptional = categoryRepository.findByDescription("Mexican");

        if(!mexicanCategoryOptional.isPresent()){
            throw new RuntimeException("Expected Category Not Found!");
        }

        Category mexicanCategory = mexicanCategoryOptional.get();

        // Guacamole
        Recipe guacamoleRecipe = new Recipe();
        guacamoleRecipe.setDescription("Perfect Guacamole");
        guacamoleRecipe.setPrepTime(10);
        guacamoleRecipe.setCookTime(0);
        guacamoleRecipe.setDifficulty(Difficulty.EASY);
        guacamoleRecipe.setDirections(GUACAMOLE_DIRECTIONS);

        // Notes
        Notes guacamoleNotes = new Notes();
        guacamoleNotes.setRecipeNotes(GUACAMOLE_NOTES);
        guacamoleRecipe.setNotes(guacamoleNotes);

        // set Ingredients
        guacamoleRecipe.addIngredient(new Ingredient("ripe avocados", new BigDecimal(2), eachUOM));
        guacamoleRecipe.addIngredient(new Ingredient("Kosher salt", new BigDecimal(".5"), teaSpoonUOM));
        guacamoleRecipe.addIngredient(new Ingredient("fresh lime juice or lemon juice", new BigDecimal("2"), tableSpoonUOM));
        guacamoleRecipe.addIngredient(new Ingredient("minced red onion or thinly sliced green onion", new BigDecimal("2"), tableSpoonUOM));
        guacamoleRecipe.addIngredient(new Ingredient("serrano chiles, stems and seeds removed, minced", new BigDecimal("2"), eachUOM));
        guacamoleRecipe.addIngredient(new Ingredient("Cilantro", new BigDecimal("2"), tableSpoonUOM));
        guacamoleRecipe.addIngredient(new Ingredient("freshly grated black pepper", new BigDecimal("2"), dashSpoonUOM));
        guacamoleRecipe.addIngredient(new Ingredient("ripe tomato, seed and pulp removed, chopped", new BigDecimal(".5"), eachUOM));

        guacamoleRecipe.getCategories().add(americanCategory);
        guacamoleRecipe.getCategories().add(mexicanCategory);

        // add to return list
        recipes.add(guacamoleRecipe);

        // Spicy Grilled Chicken Tacos
        Recipe spicyGrilledChickenTacosRecipe = new Recipe();
        spicyGrilledChickenTacosRecipe.setDescription("Spicy Grilled Chicken Taco");
        spicyGrilledChickenTacosRecipe.setCookTime(9);
        spicyGrilledChickenTacosRecipe.setPrepTime(20);
        spicyGrilledChickenTacosRecipe.setDifficulty(Difficulty.MODERATE);

        spicyGrilledChickenTacosRecipe.setDirections(SPICY_GRILLED_CHICKEN_TACO_DIRECTIONS);

        Notes spicyGrilledChickenTacosNotes = new Notes();
        spicyGrilledChickenTacosNotes.setRecipeNotes(SPICY_GRILLED_CHICKEN_TACO_NOTES);
        spicyGrilledChickenTacosRecipe.setNotes(spicyGrilledChickenTacosNotes);

        spicyGrilledChickenTacosRecipe.addIngredient(new Ingredient("Ancho Chili Powder", new BigDecimal(2), tableSpoonUOM));
        spicyGrilledChickenTacosRecipe.addIngredient(new Ingredient("Dried Oregano", new BigDecimal(1), teaSpoonUOM));
        spicyGrilledChickenTacosRecipe.addIngredient(new Ingredient("Dried Cumin", new BigDecimal(1), teaSpoonUOM));
        spicyGrilledChickenTacosRecipe.addIngredient(new Ingredient("Sugar", new BigDecimal(1), teaSpoonUOM));
        spicyGrilledChickenTacosRecipe.addIngredient(new Ingredient("Salt", new BigDecimal(".5"), teaSpoonUOM));
        spicyGrilledChickenTacosRecipe.addIngredient(new Ingredient("Clove of Garlic, Choppedr", new BigDecimal(1), eachUOM));
        spicyGrilledChickenTacosRecipe.addIngredient(new Ingredient("finely grated orange zestr", new BigDecimal(1), tableSpoonUOM));
        spicyGrilledChickenTacosRecipe.addIngredient(new Ingredient("fresh-squeezed orange juice", new BigDecimal(3), tableSpoonUOM));
        spicyGrilledChickenTacosRecipe.addIngredient(new Ingredient("Olive Oil", new BigDecimal(2), tableSpoonUOM));
        spicyGrilledChickenTacosRecipe.addIngredient(new Ingredient("boneless chicken thighs", new BigDecimal(4), tableSpoonUOM));
        spicyGrilledChickenTacosRecipe.addIngredient(new Ingredient("small corn tortillasr", new BigDecimal(8), eachUOM));
        spicyGrilledChickenTacosRecipe.addIngredient(new Ingredient("packed baby arugula", new BigDecimal(3), cupsUOM));
        spicyGrilledChickenTacosRecipe.addIngredient(new Ingredient("medium ripe avocados, slic", new BigDecimal(2), eachUOM));
        spicyGrilledChickenTacosRecipe.addIngredient(new Ingredient("radishes, thinly sliced", new BigDecimal(4), eachUOM));
        spicyGrilledChickenTacosRecipe.addIngredient(new Ingredient("cherry tomatoes, halved", new BigDecimal(".5"), pintUOM));
        spicyGrilledChickenTacosRecipe.addIngredient(new Ingredient("red onion, thinly sliced", new BigDecimal(".25"), eachUOM));
        spicyGrilledChickenTacosRecipe.addIngredient(new Ingredient("Roughly chopped cilantro", new BigDecimal(4), eachUOM));
        spicyGrilledChickenTacosRecipe.addIngredient(new Ingredient("cup sour cream thinned with 1/4 cup milk", new BigDecimal(4), cupsUOM));
        spicyGrilledChickenTacosRecipe.addIngredient(new Ingredient("lime, cut into wedges", new BigDecimal(4), eachUOM));

        spicyGrilledChickenTacosRecipe.getCategories().add(americanCategory);
        spicyGrilledChickenTacosRecipe.getCategories().add(mexicanCategory);

        recipes.add(spicyGrilledChickenTacosRecipe);
        return recipes;

    }
}
