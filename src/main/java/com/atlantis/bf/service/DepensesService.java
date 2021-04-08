package com.atlantis.bf.service;

import com.atlantis.bf.domain.Depenses;
import com.atlantis.bf.repository.DepensesRepository;
import com.atlantis.bf.service.dto.DepensesDTO;
import com.atlantis.bf.service.mapper.DepensesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRSaver;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Depenses}.
 */
@Service
@Transactional
public class DepensesService {

    private final Logger log = LoggerFactory.getLogger(DepensesService.class);

    private final DepensesRepository depensesRepository;

    private final DepensesMapper depensesMapper;

    public DepensesService(DepensesRepository depensesRepository, DepensesMapper depensesMapper) {
        this.depensesRepository = depensesRepository;
        this.depensesMapper = depensesMapper;
    }

    /**
     * Save a depenses.
     *
     * @param depensesDTO the entity to save.
     * @return the persisted entity.
     */
    public DepensesDTO save(DepensesDTO depensesDTO) {
        log.debug("Request to save Depenses : {}", depensesDTO);
        Depenses depenses = depensesMapper.toEntity(depensesDTO);
        depenses = depensesRepository.save(depenses);
        return depensesMapper.toDto(depenses);
    }

    /**
     * Get all the depenses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DepensesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Depenses");
        return depensesRepository.findAll(pageable)
            .map(depensesMapper::toDto);
    }


    /**
     * Get one depenses by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DepensesDTO> findOne(Long id) {
        log.debug("Request to get Depenses : {}", id);
        return depensesRepository.findById(id)
            .map(depensesMapper::toDto);
    }

    /**
     * Delete the depenses by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Depenses : {}", id);
        depensesRepository.deleteById(id);
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
    public String exportToPDF(LocalDate dateDebut, LocalDate dateFin, String typedepense) throws Exception{
        getDirectory("reports");
        File file = ResourceUtils.getFile("classpath:EtatDepense.jrxml");
        JasperReport jasperReport;
        jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRSaver.saveObject(jasperReport, "EtatDepense.jasper");
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(depensesRepository.findAll());
        System.out.println(dataSource);
        Map<String, Object>  params =new HashMap<>();
        params.put("dateDebut", dateDebut);
        params.put("dateFin", dateFin);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
        FileOutputStream output = new FileOutputStream("reports/"+"Etat des depenses"+".pdf"); 
        JasperExportManager.exportReportToPdfStream(jasperPrint, output); 
            output.close();
        return "reports/"+"Etat des depenses"+".pdf";   
    }
}
