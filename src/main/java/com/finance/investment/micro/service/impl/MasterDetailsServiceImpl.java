package com.finance.investment.micro.service.impl;

import com.finance.investment.micro.domain.MasterDetails;
import com.finance.investment.micro.repository.MasterDetailsRepository;
import com.finance.investment.micro.service.MasterDetailsService;
import com.finance.investment.micro.service.dto.MasterDetailsDTO;
import com.finance.investment.micro.service.mapper.MasterDetailsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MasterDetails}.
 */
@Service
@Transactional
public class MasterDetailsServiceImpl implements MasterDetailsService {

    private final Logger log = LoggerFactory.getLogger(MasterDetailsServiceImpl.class);

    private final MasterDetailsRepository masterDetailsRepository;

    private final MasterDetailsMapper masterDetailsMapper;

    public MasterDetailsServiceImpl(MasterDetailsRepository masterDetailsRepository, MasterDetailsMapper masterDetailsMapper) {
        this.masterDetailsRepository = masterDetailsRepository;
        this.masterDetailsMapper = masterDetailsMapper;
    }

    @Override
    public MasterDetailsDTO save(MasterDetailsDTO masterDetailsDTO) {
        log.debug("Request to save MasterDetails : {}", masterDetailsDTO);
        MasterDetails masterDetails = masterDetailsMapper.toEntity(masterDetailsDTO);
        masterDetails = masterDetailsRepository.save(masterDetails);
        return masterDetailsMapper.toDto(masterDetails);
    }

    @Override
    public MasterDetailsDTO update(MasterDetailsDTO masterDetailsDTO) {
        log.debug("Request to save MasterDetails : {}", masterDetailsDTO);
        MasterDetails masterDetails = masterDetailsMapper.toEntity(masterDetailsDTO);
        masterDetails = masterDetailsRepository.save(masterDetails);
        return masterDetailsMapper.toDto(masterDetails);
    }

    @Override
    public Optional<MasterDetailsDTO> partialUpdate(MasterDetailsDTO masterDetailsDTO) {
        log.debug("Request to partially update MasterDetails : {}", masterDetailsDTO);

        return masterDetailsRepository
            .findById(masterDetailsDTO.getId())
            .map(existingMasterDetails -> {
                masterDetailsMapper.partialUpdate(existingMasterDetails, masterDetailsDTO);

                return existingMasterDetails;
            })
            .map(masterDetailsRepository::save)
            .map(masterDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MasterDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MasterDetails");
        return masterDetailsRepository.findAll(pageable).map(masterDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MasterDetailsDTO> findOne(Long id) {
        log.debug("Request to get MasterDetails : {}", id);
        return masterDetailsRepository.findById(id).map(masterDetailsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MasterDetails : {}", id);
        masterDetailsRepository.deleteById(id);
    }
}
