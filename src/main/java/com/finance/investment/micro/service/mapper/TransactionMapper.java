package com.finance.investment.micro.service.mapper;

import com.finance.investment.micro.domain.Investor;
import com.finance.investment.micro.domain.Transaction;
import com.finance.investment.micro.service.dto.InvestorDTO;
import com.finance.investment.micro.service.dto.TransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transaction} and its DTO {@link TransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper extends EntityMapper<TransactionDTO, Transaction> {
    @Mapping(target = "user", source = "user", qualifiedByName = "investorId")
    TransactionDTO toDto(Transaction s);

    @Named("investorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InvestorDTO toDtoInvestorId(Investor investor);
}
