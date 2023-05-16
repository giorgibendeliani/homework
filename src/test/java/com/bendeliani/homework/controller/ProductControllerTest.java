package com.bendeliani.homework.controller;

import com.bendeliani.homework.model.Product;
import com.bendeliani.homework.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetAllProducts() throws Exception {

        Product COLA = new Product(1L, "coca-cola", 5.60, 12);
        Product SPRITE = new Product(1L, "sprite", 3.8, 10);
        Product FANTA = new Product(1L, "fanta", 6.8, 15);

        List<Product> products = Arrays.asList(COLA, SPRITE, FANTA);

        when(productRepository.findAll()).thenReturn(products);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("coca-cola"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("sprite"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("fanta"));
    }


    @Test
    void getProductById() throws Exception {
        Product COLA = new Product(1L, "coca-cola", 5.60, 12);

        when(productRepository.findById(1L)).thenReturn(Optional.of(COLA));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/products/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("coca-cola"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(5.60));

    }

    @Test
    void createProduct() throws Exception {
        Product mockProduct = new Product(1L, "coca-cola", 5.60, 12);

        when(productRepository.save(Mockito.any(Product.class))).thenReturn(mockProduct);

        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"coca-cola\",\"price\":5.6,\"amount\":12}")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("coca-cola"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(5.60));

    }

    @Test
    void updateProduct() throws Exception {
        Product updatedProduct = new Product(1L, "coca-cola", 88.00, 100);

        when(productRepository.save(Mockito.any(Product.class))).thenReturn(updatedProduct);

        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"coca-cola\",\"price\":88.00,\"amount\":100}")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("coca-cola"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(88.00));
    }

    @Test
    void deleteProduct() throws Exception {
        willDoNothing().given(productRepository).deleteById(1L);

        ResultActions response = mockMvc.perform(delete("/api/v1/products/{id}", 1L));

        response.andExpect(status().isNoContent())
                .andDo(print());
    }
}