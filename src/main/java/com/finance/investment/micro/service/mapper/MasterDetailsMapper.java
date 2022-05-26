package com.finance.investment.micro.service.mapper;

import com.finance.investment.micro.domain.MasterDetails;
import com.finance.investment.micro.service.dto.MasterDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MasterDetails} and its DTO {@link MasterDetailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface MasterDetailsMapper extends EntityMapper<MasterDetailsDTO, MasterDetails> {}
