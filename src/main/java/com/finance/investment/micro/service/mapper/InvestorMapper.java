package com.finance.investment.micro.service.mapper;

import com.finance.investment.micro.domain.Investor;
import com.finance.investment.micro.domain.InvestorPortfolio;
import com.finance.investment.micro.service.dto.InvestorDTO;
import com.finance.investment.micro.service.dto.InvestorPortfolioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Investor} and its DTO {@link InvestorDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvestorMapper extends EntityMapper<InvestorDTO, Investor> {
    @Mapping(target = "portfolio", source = "portfolio", qualifiedByName = "investorPortfolioId")
    InvestorDTO toDto(Investor s);

    @Named("investorPortfolioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InvestorPortfolioDTO toDtoInvestorPortfolioId(InvestorPortfolio investorPortfolio);
}
