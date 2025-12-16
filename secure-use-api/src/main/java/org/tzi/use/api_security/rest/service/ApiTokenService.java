package org.tzi.use.api_security.rest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import org.tzi.use.api_security.model.ApiTokenModel;
import org.tzi.use.api_security.repository.ApiTokenRepository;
import org.tzi.use.api_security.security.Role;
import org.tzi.use.api_security.token.ApiToken;

@Service
public class ApiTokenService {
    @Autowired
    private ApiTokenRepository apiTokenRepo;

    @Transactional
    public ApiToken create(UUID userId, String email) {
        ArrayList<Role> roles = new ArrayList<>();
        ApiToken apiToken;

        roles.add(new Role(Role.Roles.API_TOKEN));

        apiToken = new ApiToken(userId, email, roles);

        ApiTokenModel apiTokenModel = new ApiTokenModel();
        apiTokenModel.setUid(apiToken.getUid());
        apiTokenModel.setToken(apiToken.toTokenString());
        apiTokenModel.setOwner(userId);

        apiTokenRepo.save(apiTokenModel);

        return apiToken;
    }

    @Transactional
    public Optional<ApiToken> get(UUID tokenId, UUID ownerId) {
        ApiTokenModel apiTokenModel = this.apiTokenRepo.findByUidAndOwner(tokenId, ownerId);

        if (apiTokenModel != null) {
            return ApiToken.fromString(apiTokenModel.getToken());
        }

        return Optional.empty();
    }

    @Transactional
    public List<String> getAllAsStrings(UUID userId) {
        List<ApiTokenModel> apiTokenModelList = this.apiTokenRepo.findByOwner(userId);
        List<String> apiTokenList = new ArrayList<>();

        for (ApiTokenModel apiTokenModel : apiTokenModelList) {
            apiTokenList.add(apiTokenModel.getToken());
        }

        return apiTokenList;
    }

    @Transactional
    public int delete(UUID tokenId, UUID ownerId) {
        return this.apiTokenRepo.deleteByUidAndOwner(tokenId, ownerId);
    }
}
