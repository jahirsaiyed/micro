package com.finance.investment.micro.service.mapper;

import com.finance.investment.micro.domain.Report;
import com.finance.investment.micro.service.dto.ReportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Report} and its DTO {@link ReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportMapper extends EntityMapper<ReportDTO, Report> {}
