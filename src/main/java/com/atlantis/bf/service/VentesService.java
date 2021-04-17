package com.atlantis.bf.service;

import com.atlantis.bf.domain.Ventes;
import com.atlantis.bf.domain.Ventes_;
import com.atlantis.bf.repository.LignesVentesRepository;
import com.atlantis.bf.repository.VentesRepository;
import com.atlantis.bf.service.dto.VentesDTO;
import com.atlantis.bf.service.mapper.LignesVentesMapper;
import com.atlantis.bf.service.mapper.VentesMapper;
import com.google.common.primitives.Bytes;
import com.lowagie.text.pdf.codec.Base64.InputStream;

import org.slf4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import io.jsonwebtoken.io.IOException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import net.sf.jasperreports.engine.*;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Ventes}.
 */
@Service
@Transactional
public class VentesService {

    private final Logger log = LoggerFactory.getLogger(VentesService.class);

    private final VentesRepository ventesRepository;

    private final LignesVentesRepository lignesVentesRepository;

    private final VentesMapper ventesMapper;

    private final LignesVentesMapper lignesVentesMapper;

    @Autowired
    protected DataSource localDataSource;

    private Path fileStorageLocation;

    public VentesService(VentesRepository ventesRepository, VentesMapper ventesMapper, LignesVentesRepository lignesVentesRepository, LignesVentesMapper lignesVentesMapper) {
        this.ventesRepository = ventesRepository;
        this.ventesMapper = ventesMapper;
        this.lignesVentesRepository = lignesVentesRepository;
        this.lignesVentesMapper = lignesVentesMapper;
    }

    /**
     * Save a ventes.
     *
     * @param ventesDTO the entity to save.
     * @return the persisted entity.
     */
    public VentesDTO save(VentesDTO ventesDTO) {
        log.debug("Request to save Ventes : {}", ventesDTO);
        Ventes ventes = ventesMapper.toEntity(ventesDTO);
        ventes = ventesRepository.save(ventes);
        return ventesMapper.toDto(ventes);
    }

    /**
     * Get all the ventes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VentesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ventes");
        return ventesRepository.findAll(pageable)
            .map(ventesMapper::toDto);
    }

    /**
     * Get one ventes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VentesDTO> findOne(Long id) {
        log.debug("Request to get Ventes : {}", id);
        return ventesRepository.findById(id)
            .map(ventesMapper::toDto);
    }


    /**
     * Delete the ventes by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ventes : {}", id);
        ventesRepository.deleteById(id);
    }

    public BigDecimal venteduJour(LocalDate date) {
        List<Ventes> ventes = ventesRepository.findAll().stream().filter(v ->v.getDate().equals(date) && v.isAnnule().equals(false)).collect(Collectors.toList());     //stream().filter(vente -> vente.getDate() == date);
        BigDecimal somme =  BigDecimal.ZERO;
        System.out.println(ventes);

        for (Ventes ventes2 : ventes) {
            // if(ventes2.getDate().equals(date)){
                somme = somme.add(ventes2.getMontantFinal());
            //}
        }          
        return somme;
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
          newDirectory.setWritable(true);
            log.info("create file"+r);
            log.debug("create file"+r);
        }else {
            log.debug("not create");
            newDirectory.setWritable(true);
        }
    }

    public String exportToPDF(LocalDate date) throws Exception{
        getDirectory("reports");
        File file = ResourceUtils.getFile("classpath:ListOfVentes.jrxml");
        JasperReport jasperReport;
        //byte[] bytes = null;
        try {
            jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JRSaver.saveObject(jasperReport, "ListOfVentes.jasper");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(ventesRepository.findAll().stream().filter(p->p.getDate().equals(date)).collect(Collectors.toList()));
            System.out.println(dataSource);
            Map<String, Object>  params =new HashMap<>();
            params.put("date"," "+ date);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
            //bytes = JasperExportManager.exportReportToPdf(jasperPrint);
            //return ResponseEntity.ok( JasperExportManager.exportReportToPdf(jasperPrint));
            // JasperExportManager.exportReportToPdfFile(jasperPrint, ".\\recap\\Ventes-du-"+date+".pdf");
            FileOutputStream output = new FileOutputStream("reports/"+"Ventes du "+date+".pdf"); 
            JasperExportManager.exportReportToPdfStream(jasperPrint, output); 
            output.close();
        return "reports/"+"Ventes du "+date+".pdf";   
            
        } catch (JRException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";    
    }

    public ResponseEntity<byte[]> imprimerRecu(Long venteId) throws Exception{
        Ventes vente = ventesRepository.getOne(venteId);
        File file = ResourceUtils.getFile("classpath:recu.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(vente.getLignesVentes());
        Map<String, Object>  params =new HashMap<>();
        params.put("client"," "+ vente.getClient().getFullName());
        params.put("date", vente.getDate());
        params.put("numero", "AT"+vente.getId());
        params.put("somme", vente.getMontantInitial());
        params.put("remise", vente.getRemise());
        params.put("total", vente.getMontantFinal());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
        return ResponseEntity.ok( JasperExportManager.exportReportToPdf(jasperPrint));   
    }


}
