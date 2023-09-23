package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {
    public static final long ID = 1L;
    RecipeService recipeService;

    @Mock
    RecipeRepository recipeRepository;
    @BeforeEach
    void setUp() {
        recipeService = new RecipeServiceImpl(recipeRepository);
    }

    @Test
    void recipeGetSizeTest(){
        Recipe recipe = new Recipe();
        HashSet<Recipe> recipes = new HashSet<>();
        recipes.add(recipe);
        Mockito.when(recipeRepository.findAll()).thenReturn(recipes);

        Set<Recipe> result = recipeService.findAllRecipes();
        assertEquals(1, result.size());
        Mockito.verify(recipeRepository, Mockito.times(1)).findAll();
    }

    @Test
    void recipeByIdTest(){
        Recipe recipe = new Recipe();
        recipe.setId(ID);
        Mockito.when(recipeRepository.findById(Mockito.any())).thenReturn(Optional.of(recipe));

        Recipe actualRecipe = recipeService.findById(ID);
        assertNotNull(actualRecipe);
        assertEquals(ID, actualRecipe.getId());
        Mockito.verify(recipeRepository, Mockito.times(1)).findById(Mockito.any());
    }
}