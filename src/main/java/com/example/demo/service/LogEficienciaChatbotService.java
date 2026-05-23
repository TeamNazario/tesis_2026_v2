package com.example.demo.service;

import com.example.demo.dto.LogEficienciaChatbotRequest;
import com.example.demo.dto.LogEficienciaChatbotResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.LogMapper;
import com.example.demo.model.LogEficienciaChatbot;
import com.example.demo.repository.LogEficienciaChatbotRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogEficienciaChatbotService extends CrudService<LogEficienciaChatbot, Long> {
    private final LogEficienciaChatbotRepository logRepository;
    private final LogMapper mapper;

    public LogEficienciaChatbotService(LogEficienciaChatbotRepository repository, LogMapper mapper) {
        super(repository, "LogEficienciaChatbot");
        this.logRepository = repository;
        this.mapper = mapper;
    }

    @Override
    protected Long parseId(String id) {
        return parseLongId(id);
    }

    @Transactional(readOnly = true)
    public List<LogEficienciaChatbotResponse> findAllDto() {
        return logRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public LogEficienciaChatbotResponse findDtoById(Long id) {
        return mapper.toResponse(findLog(id));
    }

    @Transactional
    public LogEficienciaChatbotResponse create(LogEficienciaChatbotRequest request) {
        return mapper.toResponse(logRepository.save(mapper.toEntity(request)));
    }

    @Transactional
    public LogEficienciaChatbotResponse update(Long id, LogEficienciaChatbotRequest request) {
        LogEficienciaChatbot log = findLog(id);
        mapper.updateEntity(log, request);
        return mapper.toResponse(logRepository.save(log));
    }

    @Transactional
    public void deleteById(Long id) {
        if (!logRepository.existsById(id)) {
            throw new ResourceNotFoundException("LogEficienciaChatbot", id);
        }
        logRepository.deleteById(id);
    }

    private LogEficienciaChatbot findLog(Long id) {
        return logRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LogEficienciaChatbot", id));
    }
}
