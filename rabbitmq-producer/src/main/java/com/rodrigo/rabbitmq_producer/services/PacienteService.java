package com.rodrigo.rabbitmq_producer.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodrigo.rabbitmq_producer.models.PacienteModel;
import com.rodrigo.rabbitmq_producer.producers.PacienteProducer;
import com.rodrigo.rabbitmq_producer.repositorys.PacienteRepository;

import jakarta.transaction.Transactional;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private PacienteProducer pacienteProducer;

    @Transactional
    public PacienteModel saveOrUpdate(PacienteModel pacienteModel) {
        Optional<PacienteModel> existingPacienteOptional = pacienteRepository.findByCpf(pacienteModel.getCpf());

        PacienteModel savedOrUpdatedPaciente;

        if (existingPacienteOptional.isPresent()) {
            PacienteModel existingPaciente = existingPacienteOptional.get();
            BeanUtils.copyProperties(pacienteModel, existingPaciente, "id"); // Copia propriedades exceto o id
            savedOrUpdatedPaciente = pacienteRepository.save(existingPaciente); // Atualiza o paciente existente
        } else {
            savedOrUpdatedPaciente = pacienteRepository.save(pacienteModel); // Salva um novo paciente
        }

        pacienteProducer.publishMessagePaciente(savedOrUpdatedPaciente); // Envia mensagem para o RabbitMQ após a
                                                                         // persistência

        return savedOrUpdatedPaciente;
    }

    public PacienteModel findByid(Long id) {
        Optional<PacienteModel> pacienteModel = pacienteRepository.findById(id);
        return pacienteModel.orElse(null);
    }

    public List<PacienteModel> findAll() {
        return pacienteRepository.findAll();
    }
}
