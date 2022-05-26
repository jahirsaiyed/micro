package com.finance.investment.micro.service.mapper;

import com.finance.investment.micro.domain.InvestorPortfolio;
import com.finance.investment.micro.service.dto.InvestorPortfolioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InvestorPortfolio} and its DTO {@link InvestorPortfolioDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvestorPortfolioMapper extends EntityMapper<InvestorPortfolioDTO, InvestorPortfolio> {}
