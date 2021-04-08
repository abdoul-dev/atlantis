package com.atlantis.bf.web.rest;

import com.atlantis.bf.AtlantisPoissonnerieApp;
import com.atlantis.bf.domain.Products;
import com.atlantis.bf.domain.LigneEntreeStock;
import com.atlantis.bf.domain.LignesVentes;
import com.atlantis.bf.domain.LignesReservation;
import com.atlantis.bf.domain.TypeProduit;
import com.atlantis.bf.repository.ProductsRepository;
import com.atlantis.bf.service.ProductsService;
import com.atlantis.bf.service.dto.ProductsDTO;
import com.atlantis.bf.service.mapper.ProductsMapper;
import com.atlantis.bf.service.dto.ProductsCriteria;
import com.atlantis.bf.service.ProductsQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProductsResource} REST controller.
 */
@SpringBootTest(classes = AtlantisPoissonnerieApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProductsResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRIX_VENTE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRIX_VENTE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRIX_VENTE = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_IS_DISABLED = false;
    private static final Boolean UPDATED_IS_DISABLED = true;

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private ProductsMapper productsMapper;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private ProductsQueryService productsQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductsMockMvc;

    private Products products;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Products createEntity(EntityManager em) {
        Products products = new Products()
            .libelle(DEFAULT_LIBELLE)
            .prixVente(DEFAULT_PRIX_VENTE)
            .isDisabled(DEFAULT_IS_DISABLED)
            .comments(DEFAULT_COMMENTS);
        return products;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Products createUpdatedEntity(EntityManager em) {
        Products products = new Products()
            .libelle(UPDATED_LIBELLE)
            .prixVente(UPDATED_PRIX_VENTE)
            .isDisabled(UPDATED_IS_DISABLED)
            .comments(UPDATED_COMMENTS);
        return products;
    }

    @BeforeEach
    public void initTest() {
        products = createEntity(em);
    }

    @Test
    @Transactional
    public void createProducts() throws Exception {
        int databaseSizeBeforeCreate = productsRepository.findAll().size();
        // Create the Products
        ProductsDTO productsDTO = productsMapper.toDto(products);
        restProductsMockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productsDTO)))
            .andExpect(status().isCreated());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeCreate + 1);
        Products testProducts = productsList.get(productsList.size() - 1);
        assertThat(testProducts.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testProducts.getPrixVente()).isEqualTo(DEFAULT_PRIX_VENTE);
        assertThat(testProducts.isIsDisabled()).isEqualTo(DEFAULT_IS_DISABLED);
        assertThat(testProducts.getComments()).isEqualTo(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    public void createProductsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productsRepository.findAll().size();

        // Create the Products with an existing ID
        products.setId(1L);
        ProductsDTO productsDTO = productsMapper.toDto(products);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductsMockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = productsRepository.findAll().size();
        // set the field null
        products.setLibelle(null);

        // Create the Products, which fails.
        ProductsDTO productsDTO = productsMapper.toDto(products);


        restProductsMockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productsDTO)))
            .andExpect(status().isBadRequest());

        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrixVenteIsRequired() throws Exception {
        int databaseSizeBeforeTest = productsRepository.findAll().size();
        // set the field null
        products.setPrixVente(null);

        // Create the Products, which fails.
        ProductsDTO productsDTO = productsMapper.toDto(products);


        restProductsMockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productsDTO)))
            .andExpect(status().isBadRequest());

        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsDisabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = productsRepository.findAll().size();
        // set the field null
        products.setIsDisabled(null);

        // Create the Products, which fails.
        ProductsDTO productsDTO = productsMapper.toDto(products);


        restProductsMockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productsDTO)))
            .andExpect(status().isBadRequest());

        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList
        restProductsMockMvc.perform(get("/api/products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(products.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].prixVente").value(hasItem(DEFAULT_PRIX_VENTE.intValue())))
            .andExpect(jsonPath("$.[*].isDisabled").value(hasItem(DEFAULT_IS_DISABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }
    
    @Test
    @Transactional
    public void getProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get the products
        restProductsMockMvc.perform(get("/api/products/{id}", products.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(products.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.prixVente").value(DEFAULT_PRIX_VENTE.intValue()))
            .andExpect(jsonPath("$.isDisabled").value(DEFAULT_IS_DISABLED.booleanValue()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }


    @Test
    @Transactional
    public void getProductsByIdFiltering() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        Long id = products.getId();

        defaultProductsShouldBeFound("id.equals=" + id);
        defaultProductsShouldNotBeFound("id.notEquals=" + id);

        defaultProductsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductsShouldNotBeFound("id.greaterThan=" + id);

        defaultProductsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProductsByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where libelle equals to DEFAULT_LIBELLE
        defaultProductsShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the productsList where libelle equals to UPDATED_LIBELLE
        defaultProductsShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllProductsByLibelleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where libelle not equals to DEFAULT_LIBELLE
        defaultProductsShouldNotBeFound("libelle.notEquals=" + DEFAULT_LIBELLE);

        // Get all the productsList where libelle not equals to UPDATED_LIBELLE
        defaultProductsShouldBeFound("libelle.notEquals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllProductsByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultProductsShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the productsList where libelle equals to UPDATED_LIBELLE
        defaultProductsShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllProductsByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where libelle is not null
        defaultProductsShouldBeFound("libelle.specified=true");

        // Get all the productsList where libelle is null
        defaultProductsShouldNotBeFound("libelle.specified=false");
    }
                @Test
    @Transactional
    public void getAllProductsByLibelleContainsSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where libelle contains DEFAULT_LIBELLE
        defaultProductsShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the productsList where libelle contains UPDATED_LIBELLE
        defaultProductsShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllProductsByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where libelle does not contain DEFAULT_LIBELLE
        defaultProductsShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the productsList where libelle does not contain UPDATED_LIBELLE
        defaultProductsShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }


    @Test
    @Transactional
    public void getAllProductsByPrixVenteIsEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where prixVente equals to DEFAULT_PRIX_VENTE
        defaultProductsShouldBeFound("prixVente.equals=" + DEFAULT_PRIX_VENTE);

        // Get all the productsList where prixVente equals to UPDATED_PRIX_VENTE
        defaultProductsShouldNotBeFound("prixVente.equals=" + UPDATED_PRIX_VENTE);
    }

    @Test
    @Transactional
    public void getAllProductsByPrixVenteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where prixVente not equals to DEFAULT_PRIX_VENTE
        defaultProductsShouldNotBeFound("prixVente.notEquals=" + DEFAULT_PRIX_VENTE);

        // Get all the productsList where prixVente not equals to UPDATED_PRIX_VENTE
        defaultProductsShouldBeFound("prixVente.notEquals=" + UPDATED_PRIX_VENTE);
    }

    @Test
    @Transactional
    public void getAllProductsByPrixVenteIsInShouldWork() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where prixVente in DEFAULT_PRIX_VENTE or UPDATED_PRIX_VENTE
        defaultProductsShouldBeFound("prixVente.in=" + DEFAULT_PRIX_VENTE + "," + UPDATED_PRIX_VENTE);

        // Get all the productsList where prixVente equals to UPDATED_PRIX_VENTE
        defaultProductsShouldNotBeFound("prixVente.in=" + UPDATED_PRIX_VENTE);
    }

    @Test
    @Transactional
    public void getAllProductsByPrixVenteIsNullOrNotNull() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where prixVente is not null
        defaultProductsShouldBeFound("prixVente.specified=true");

        // Get all the productsList where prixVente is null
        defaultProductsShouldNotBeFound("prixVente.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByPrixVenteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where prixVente is greater than or equal to DEFAULT_PRIX_VENTE
        defaultProductsShouldBeFound("prixVente.greaterThanOrEqual=" + DEFAULT_PRIX_VENTE);

        // Get all the productsList where prixVente is greater than or equal to UPDATED_PRIX_VENTE
        defaultProductsShouldNotBeFound("prixVente.greaterThanOrEqual=" + UPDATED_PRIX_VENTE);
    }

    @Test
    @Transactional
    public void getAllProductsByPrixVenteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where prixVente is less than or equal to DEFAULT_PRIX_VENTE
        defaultProductsShouldBeFound("prixVente.lessThanOrEqual=" + DEFAULT_PRIX_VENTE);

        // Get all the productsList where prixVente is less than or equal to SMALLER_PRIX_VENTE
        defaultProductsShouldNotBeFound("prixVente.lessThanOrEqual=" + SMALLER_PRIX_VENTE);
    }

    @Test
    @Transactional
    public void getAllProductsByPrixVenteIsLessThanSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where prixVente is less than DEFAULT_PRIX_VENTE
        defaultProductsShouldNotBeFound("prixVente.lessThan=" + DEFAULT_PRIX_VENTE);

        // Get all the productsList where prixVente is less than UPDATED_PRIX_VENTE
        defaultProductsShouldBeFound("prixVente.lessThan=" + UPDATED_PRIX_VENTE);
    }

    @Test
    @Transactional
    public void getAllProductsByPrixVenteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where prixVente is greater than DEFAULT_PRIX_VENTE
        defaultProductsShouldNotBeFound("prixVente.greaterThan=" + DEFAULT_PRIX_VENTE);

        // Get all the productsList where prixVente is greater than SMALLER_PRIX_VENTE
        defaultProductsShouldBeFound("prixVente.greaterThan=" + SMALLER_PRIX_VENTE);
    }


    @Test
    @Transactional
    public void getAllProductsByIsDisabledIsEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where isDisabled equals to DEFAULT_IS_DISABLED
        defaultProductsShouldBeFound("isDisabled.equals=" + DEFAULT_IS_DISABLED);

        // Get all the productsList where isDisabled equals to UPDATED_IS_DISABLED
        defaultProductsShouldNotBeFound("isDisabled.equals=" + UPDATED_IS_DISABLED);
    }

    @Test
    @Transactional
    public void getAllProductsByIsDisabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where isDisabled not equals to DEFAULT_IS_DISABLED
        defaultProductsShouldNotBeFound("isDisabled.notEquals=" + DEFAULT_IS_DISABLED);

        // Get all the productsList where isDisabled not equals to UPDATED_IS_DISABLED
        defaultProductsShouldBeFound("isDisabled.notEquals=" + UPDATED_IS_DISABLED);
    }

    @Test
    @Transactional
    public void getAllProductsByIsDisabledIsInShouldWork() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where isDisabled in DEFAULT_IS_DISABLED or UPDATED_IS_DISABLED
        defaultProductsShouldBeFound("isDisabled.in=" + DEFAULT_IS_DISABLED + "," + UPDATED_IS_DISABLED);

        // Get all the productsList where isDisabled equals to UPDATED_IS_DISABLED
        defaultProductsShouldNotBeFound("isDisabled.in=" + UPDATED_IS_DISABLED);
    }

    @Test
    @Transactional
    public void getAllProductsByIsDisabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where isDisabled is not null
        defaultProductsShouldBeFound("isDisabled.specified=true");

        // Get all the productsList where isDisabled is null
        defaultProductsShouldNotBeFound("isDisabled.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where comments equals to DEFAULT_COMMENTS
        defaultProductsShouldBeFound("comments.equals=" + DEFAULT_COMMENTS);

        // Get all the productsList where comments equals to UPDATED_COMMENTS
        defaultProductsShouldNotBeFound("comments.equals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllProductsByCommentsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where comments not equals to DEFAULT_COMMENTS
        defaultProductsShouldNotBeFound("comments.notEquals=" + DEFAULT_COMMENTS);

        // Get all the productsList where comments not equals to UPDATED_COMMENTS
        defaultProductsShouldBeFound("comments.notEquals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllProductsByCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where comments in DEFAULT_COMMENTS or UPDATED_COMMENTS
        defaultProductsShouldBeFound("comments.in=" + DEFAULT_COMMENTS + "," + UPDATED_COMMENTS);

        // Get all the productsList where comments equals to UPDATED_COMMENTS
        defaultProductsShouldNotBeFound("comments.in=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllProductsByCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where comments is not null
        defaultProductsShouldBeFound("comments.specified=true");

        // Get all the productsList where comments is null
        defaultProductsShouldNotBeFound("comments.specified=false");
    }
                @Test
    @Transactional
    public void getAllProductsByCommentsContainsSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where comments contains DEFAULT_COMMENTS
        defaultProductsShouldBeFound("comments.contains=" + DEFAULT_COMMENTS);

        // Get all the productsList where comments contains UPDATED_COMMENTS
        defaultProductsShouldNotBeFound("comments.contains=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllProductsByCommentsNotContainsSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where comments does not contain DEFAULT_COMMENTS
        defaultProductsShouldNotBeFound("comments.doesNotContain=" + DEFAULT_COMMENTS);

        // Get all the productsList where comments does not contain UPDATED_COMMENTS
        defaultProductsShouldBeFound("comments.doesNotContain=" + UPDATED_COMMENTS);
    }


    @Test
    @Transactional
    public void getAllProductsByLigneStockIsEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);
        LigneEntreeStock ligneStock = LigneEntreeStockResourceIT.createEntity(em);
        em.persist(ligneStock);
        em.flush();
        products.addLigneStock(ligneStock);
        productsRepository.saveAndFlush(products);
        Long ligneStockId = ligneStock.getId();

        // Get all the productsList where ligneStock equals to ligneStockId
        defaultProductsShouldBeFound("ligneStockId.equals=" + ligneStockId);

        // Get all the productsList where ligneStock equals to ligneStockId + 1
        defaultProductsShouldNotBeFound("ligneStockId.equals=" + (ligneStockId + 1));
    }


    @Test
    @Transactional
    public void getAllProductsByLigneVentesIsEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);
        LignesVentes ligneVentes = LignesVentesResourceIT.createEntity(em);
        em.persist(ligneVentes);
        em.flush();
        products.addLigneVentes(ligneVentes);
        productsRepository.saveAndFlush(products);
        Long ligneVentesId = ligneVentes.getId();

        // Get all the productsList where ligneVentes equals to ligneVentesId
        defaultProductsShouldBeFound("ligneVentesId.equals=" + ligneVentesId);

        // Get all the productsList where ligneVentes equals to ligneVentesId + 1
        defaultProductsShouldNotBeFound("ligneVentesId.equals=" + (ligneVentesId + 1));
    }


    @Test
    @Transactional
    public void getAllProductsByLigneReservationIsEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);
        LignesReservation ligneReservation = LignesReservationResourceIT.createEntity(em);
        em.persist(ligneReservation);
        em.flush();
        products.addLigneReservation(ligneReservation);
        productsRepository.saveAndFlush(products);
        Long ligneReservationId = ligneReservation.getId();

        // Get all the productsList where ligneReservation equals to ligneReservationId
        defaultProductsShouldBeFound("ligneReservationId.equals=" + ligneReservationId);

        // Get all the productsList where ligneReservation equals to ligneReservationId + 1
        defaultProductsShouldNotBeFound("ligneReservationId.equals=" + (ligneReservationId + 1));
    }


    @Test
    @Transactional
    public void getAllProductsByTypeProduitIsEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);
        TypeProduit typeProduit = TypeProduitResourceIT.createEntity(em);
        em.persist(typeProduit);
        em.flush();
        products.setTypeProduit(typeProduit);
        productsRepository.saveAndFlush(products);
        Long typeProduitId = typeProduit.getId();

        // Get all the productsList where typeProduit equals to typeProduitId
        defaultProductsShouldBeFound("typeProduitId.equals=" + typeProduitId);

        // Get all the productsList where typeProduit equals to typeProduitId + 1
        defaultProductsShouldNotBeFound("typeProduitId.equals=" + (typeProduitId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductsShouldBeFound(String filter) throws Exception {
        restProductsMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(products.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].prixVente").value(hasItem(DEFAULT_PRIX_VENTE.intValue())))
            .andExpect(jsonPath("$.[*].isDisabled").value(hasItem(DEFAULT_IS_DISABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));

        // Check, that the count call also returns 1
        restProductsMockMvc.perform(get("/api/products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductsShouldNotBeFound(String filter) throws Exception {
        restProductsMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductsMockMvc.perform(get("/api/products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingProducts() throws Exception {
        // Get the products
        restProductsMockMvc.perform(get("/api/products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        int databaseSizeBeforeUpdate = productsRepository.findAll().size();

        // Update the products
        Products updatedProducts = productsRepository.findById(products.getId()).get();
        // Disconnect from session so that the updates on updatedProducts are not directly saved in db
        em.detach(updatedProducts);
        updatedProducts
            .libelle(UPDATED_LIBELLE)
            .prixVente(UPDATED_PRIX_VENTE)
            .isDisabled(UPDATED_IS_DISABLED)
            .comments(UPDATED_COMMENTS);
        ProductsDTO productsDTO = productsMapper.toDto(updatedProducts);

        restProductsMockMvc.perform(put("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productsDTO)))
            .andExpect(status().isOk());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
        Products testProducts = productsList.get(productsList.size() - 1);
        assertThat(testProducts.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testProducts.getPrixVente()).isEqualTo(UPDATED_PRIX_VENTE);
        assertThat(testProducts.isIsDisabled()).isEqualTo(UPDATED_IS_DISABLED);
        assertThat(testProducts.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void updateNonExistingProducts() throws Exception {
        int databaseSizeBeforeUpdate = productsRepository.findAll().size();

        // Create the Products
        ProductsDTO productsDTO = productsMapper.toDto(products);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductsMockMvc.perform(put("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        int databaseSizeBeforeDelete = productsRepository.findAll().size();

        // Delete the products
        restProductsMockMvc.perform(delete("/api/products/{id}", products.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
