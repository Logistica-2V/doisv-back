package com.logistica.doisv.services;

import com.logistica.doisv.entities.Licenca;
import com.logistica.doisv.entities.enums.Status;
import com.logistica.doisv.repositories.LicencaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LicencaService {

    @Autowired
    private LicencaRepository licencaRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    protected void validarLicenca(){
        List<Licenca> licencas = licencaRepository.findAllByStatus(Status.ATIVO);

        licencas.stream()
                .filter(l -> l.getValidade().isBefore(LocalDate.now()))
                .forEach(l -> l.setStatus(Status.INATIVO));
    }
}
