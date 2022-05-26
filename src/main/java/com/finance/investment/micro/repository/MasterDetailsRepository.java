package com.finance.investment.micro.repository;

import com.finance.investment.micro.domain.MasterDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MasterDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MasterDetailsRepository extends JpaRepository<MasterDetails, Long> {}
