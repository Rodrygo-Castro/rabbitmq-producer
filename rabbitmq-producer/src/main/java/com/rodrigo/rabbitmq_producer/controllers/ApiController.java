package com.rodrigo.rabbitmq_producer.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodrigo.rabbitmq_producer.dtos.PacienteRecordDto;
import com.rodrigo.rabbitmq_producer.models.PacienteModel;
import com.rodrigo.rabbitmq_producer.services.PacienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final PacienteService pacienteService;

    public ApiController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping("/paciente")
    public ResponseEntity<PacienteModel> apiPaciente(@RequestBody @Valid PacienteRecordDto pacienteRecordDto) {
        PacienteModel pacienteModel = convertDtoToModel(pacienteRecordDto);
        PacienteModel savedPaciente = pacienteService.saveOrUpdate(pacienteModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPaciente);
    }

    private PacienteModel convertDtoToModel(PacienteRecordDto pacienteRecordDto) {
        PacienteModel pacienteModel = new PacienteModel();
        BeanUtils.copyProperties(pacienteRecordDto, pacienteModel);
        return pacienteModel;
    }
}
