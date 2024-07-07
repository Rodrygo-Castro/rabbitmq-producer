package com.rodrigo.rabbitmq_producer.producers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rodrigo.rabbitmq_producer.dtos.PacienteDto;
import com.rodrigo.rabbitmq_producer.models.PacienteModel;

@Component
public class PacienteProducer {

    private static final Logger logger = LoggerFactory.getLogger(PacienteProducer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value(value = "${broker.queue.patient.name}")
    private String routingKey;

    public void publishMessagePaciente(PacienteModel pacienteModel) {
        PacienteDto pacienteDto = convertToDto(pacienteModel);
        
        try {
            rabbitTemplate.convertAndSend("", routingKey, pacienteDto);
            logger.info("Mensagem enviada para a fila {}: {}", routingKey, pacienteDto);
        } catch (Exception e) {
            logger.error("Erro ao enviar mensagem para a fila {}: {}", routingKey, pacienteDto, e);
        }
    }

    private PacienteDto convertToDto(PacienteModel pacienteModel) {
        PacienteDto pacienteDto = new PacienteDto();
        pacienteDto.setId(pacienteModel.getId());
        pacienteDto.setNomeCompleto(pacienteModel.getNomeCompleto());
        pacienteDto.setCpf(pacienteModel.getCpf());
        pacienteDto.setCelular(pacienteModel.getCelular());
        pacienteDto.setEmail(pacienteModel.getEmail());
        pacienteDto.setDataNascimento(pacienteModel.getDataNascimento());
        pacienteDto.setSexo(pacienteModel.getSexo());
        pacienteDto.setBairro(pacienteModel.getBairro());
        pacienteDto.setCep(pacienteModel.getCep());
        pacienteDto.setCidade(pacienteModel.getCidade());
        pacienteDto.setEstado(pacienteModel.getEstado());
        pacienteDto.setNumero(pacienteModel.getNumero());
        pacienteDto.setGestante(pacienteModel.getGestante());
        pacienteDto.setFebre(pacienteModel.getFebre());
        pacienteDto.setRua(pacienteModel.getRua());
        pacienteDto.setDiagnostico(pacienteModel.getDiagnostico());
        pacienteDto.setSintomas(pacienteModel.getSintomas());
        pacienteDto.setExame(pacienteModel.getExame());
        
        return pacienteDto;
    }
}
