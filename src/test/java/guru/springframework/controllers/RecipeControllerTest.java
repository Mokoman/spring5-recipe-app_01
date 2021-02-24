package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RecipeControllerTest {

    public static final long RECIPE_ID = 1L;
    @Mock
    RecipeService recipeService;

    @InjectMocks
    RecipeController controller;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        controller = new RecipeController(recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void showRecipeForId() {
        Recipe recipe = Recipe.builder().id(RECIPE_ID).build();

        when(recipeService.findById(anyLong())).thenReturn(recipe);

        try {
            mockMvc.perform(get("/recipe/1/show"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("recipe/show"))
                    .andExpect(model().attributeExists("recipe"));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void showRecipeForIdNotFound_ShouldThrowException() {
        when(recipeService.findById(anyLong())).thenThrow(NotFoundException.class);

        try {
            mockMvc.perform(get("/recipe/1/show"))
                    .andExpect(status().isNotFound())
                    .andExpect(view().name("errors/404notfound"));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void showRecipeForIdWithBadStringId_ShouldThrowException() {
        when(recipeService.findById(anyLong())).thenThrow(NumberFormatException.class);

        try {
            mockMvc.perform(get("/recipe/dummy/show"))
                    .andExpect(status().isBadRequest())
                    .andExpect(view().name("errors/400badrequest"));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void createRecipeForId() {
        try {
            mockMvc.perform(get("/recipe/create"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("recipe/recipe_form"))
                    .andExpect(model().attributeExists("recipe"));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void updateRecipeForId() {
        Recipe recipe = Recipe.builder().id(RECIPE_ID).build();

        when(recipeService.findById(anyLong())).thenReturn(recipe);

        try {
            mockMvc.perform(get("/recipe/1/update"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("recipe/recipe_form"))
                    .andExpect(model().attributeExists("recipe"));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void deleteRecipeById() {

        try {
            mockMvc.perform(get("/recipe/1/delete"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/"));
        } catch (Exception e) {
            fail();
        }
    }
}