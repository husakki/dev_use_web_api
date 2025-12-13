package com.secure_use_api.rest.service;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import com.secure_use_api.exceptions.Exceptions.ExpiredException;
import com.secure_use_api.exceptions.Exceptions.InvalidException;
import com.secure_use_api.model.RefreshTokenModel;
import com.secure_use_api.repository.RefreshTokenRepository;
import com.secure_use_api.token.AccessToken;
import com.secure_use_api.token.RefreshToken;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepo;

    @Transactional
    public RefreshToken create(AccessToken accessToken) {
        RefreshToken refreshToken = new RefreshToken(accessToken.getUid());

        RefreshTokenModel refreshTokenModel = new RefreshTokenModel();
        refreshTokenModel.setUid(refreshToken.getUid());
        refreshTokenModel.setSpouse(refreshToken.getSpouseId());
        refreshTokenModel.setExpireAt(new Timestamp(refreshToken.getExpiresAt().getTime()));

        refreshTokenRepo.save(refreshTokenModel);

        return refreshToken;
    }

    @Transactional
    public AccessToken devaluate(RefreshToken refreshToken, AccessToken spouseToken)
            throws ExpiredException, InvalidException {
        Date currentTime = new Date();

        // Check if expiredAt Date is in the past
        if (refreshToken.getExpiresAt().before(currentTime)) {
            throw new ExpiredException("RefreshToken is expired");
        }

        int deletedCount = refreshTokenRepo.deleteExpiredToken(refreshToken.getUid(), spouseToken.getUid(),
                currentTime);

        if (deletedCount == 0) {
            throw new InvalidException("RefreshToken is not valid");
        }

        AccessToken accessToken = new AccessToken(spouseToken.getUserId(), spouseToken.getEmail(),
                spouseToken.getRoles());

        return accessToken;
    }
}
