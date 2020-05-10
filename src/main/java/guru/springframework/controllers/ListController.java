package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Slf4j
@RequestMapping("/recipes")
@Controller
public class ListController {
    private final RecipeService recipeService;

    public ListController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"", "/"})
    public String getRecipes(Model model) {
        final Set<Recipe> recipes = recipeService.getRecipes();
        model.addAttribute("recipes", recipes);
        log.debug("Added to model: {} recipes", recipes.size());
        return "recipes/list";
    }
}
