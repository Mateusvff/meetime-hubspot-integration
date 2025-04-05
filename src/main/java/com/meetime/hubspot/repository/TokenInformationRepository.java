package com.meetime.hubspot.repository;

import com.meetime.hubspot.model.TokenInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TokenInformationRepository extends JpaRepository<TokenInformation, UUID> {

    Optional<TokenInformation> findTopByOrderByExpiresAtDesc();

}