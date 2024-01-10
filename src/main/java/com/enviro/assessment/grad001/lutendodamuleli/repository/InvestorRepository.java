package com.enviro.assessment.grad001.lutendodamuleli.repository;

import com.enviro.assessment.grad001.lutendodamuleli.entity.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, Long> {
}
