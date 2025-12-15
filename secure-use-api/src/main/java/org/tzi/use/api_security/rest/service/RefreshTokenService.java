package org.tzi.use.api_security.rest.service;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import org.tzi.use.api_security.exceptions.Exceptions.ExpiredException;
import org.tzi.use.api_security.exceptions.Exceptions.InvalidException;
import org.tzi.use.api_security.model.RefreshTokenModel;
import org.tzi.use.api_security.repository.RefreshTokenRepository;
import org.tzi.use.api_security.token.AccessToken;
import org.tzi.use.api_security.token.RefreshToken;

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
