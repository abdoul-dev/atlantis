package com.atlantis.bf.service;

import com.atlantis.bf.domain.Products;
import com.atlantis.bf.repository.ProductsRepository;
import com.atlantis.bf.service.dto.ProductsDTO;
import com.atlantis.bf.service.mapper.ProductsMapper;
import com.lowagie.text.pdf.codec.Base64.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import antlr.collections.List;
import io.undertow.server.handlers.resource.Resource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Products}.
 */
@Service
@Transactional
public class ProductsService {

    private final Logger log = LoggerFactory.getLogger(ProductsService.class);

    private final ProductsRepository productsRepository;

    private final ProductsMapper productsMapper;
    private final ApplicationContext context;


    public ProductsService(ApplicationContext context, ProductsRepository productsRepository, ProductsMapper productsMapper) {
        this.context = context;
        this.productsRepository = productsRepository;
        this.productsMapper = productsMapper;
    }

    /**
     * Save a products.
     *
     * @param productsDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductsDTO save(ProductsDTO productsDTO) {
        log.debug("Request to save Products : {}", productsDTO);
        Products products = productsMapper.toEntity(productsDTO);
        products = productsRepository.save(products);
        return productsMapper.toDto(products);
    }

    /**
     * Get all the products.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productsRepository.findAll(pageable)
            .map(productsMapper::toDto);
    }


    /**
     * Get one products by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductsDTO> findOne(Long id) {
        log.debug("Request to get Products : {}", id);
        return productsRepository.findById(id)
            .map(productsMapper::toDto);
    }

    /**
     * Delete the products by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Products : {}", id);
        productsRepository.deleteById(id);
    }

    /**
     * Creation du repertoire de stockage des fichiers de l'applications
     *
     * @throws IOException
     */
    private void getDirectory(String name) {
        File newDirectory = new File(name);
        if (!newDirectory.exists()) {
          Boolean r=  newDirectory.mkdir();
            log.info("create file"+r);
            log.debug("create file"+r);
            newDirectory.setWritable(true);
        }else {
            log.debug("not create");
        }
    }
    
    public String exportToPDF() throws JRException, IOException{
            
        getDirectory("reports");
        File file = ResourceUtils.getFile("classpath:ListOfProducts.jrxml");
        JasperReport jasperReport;
        jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRSaver.saveObject(jasperReport, "ListOfProducts.jasper");
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(productsRepository.findAll());
        System.out.println(dataSource);
        Map<String, Object>  params =new HashMap<>();
        params.put("type"," ");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
        // JasperExportManager.exportReportToPdf(jasperPrint);
        // JasperExportManager.exportReportToPdfFile(jasperPrint, ".\\recap\\Listes_des_produits-"+".pdf");
        FileOutputStream output = new FileOutputStream("reports/"+"Liste de produits"+".pdf"); 
        JasperExportManager.exportReportToPdfStream(jasperPrint, output); 
            output.close();
        return "reports/"+"Liste de produits"+".pdf";   
        
    }

}
