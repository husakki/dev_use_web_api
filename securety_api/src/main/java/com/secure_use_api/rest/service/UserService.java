package com.secure_use_api.rest.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import com.secure_use_api.model.UserAdditionsModel;
import com.secure_use_api.model.UserMetaModel;
import com.secure_use_api.repository.UserAdditionsRepository;
import com.secure_use_api.repository.UserMetaRepository;
import com.secure_use_api.util.Config;

@Service
public class UserService {

    public static int timeLimitPerDayInSeconds = Config.getInstance().getResourceLimitPerDay();

    @Autowired
    private UserMetaRepository userMetaRepo;

    @Autowired
    private UserAdditionsRepository userAdditionsRepo;

    @Transactional
    public void createUserAdditions(UUID userId) {
        // This is a db call which is needed to get the userMeta.
        // Other approaches are getting it as parameter which require returning
        // the Entity by the other service function (exposing the Model)
        // The other would be using a single Service doing all stuff
        // which creates a tighter coupling causing less flexible and separation of
        // concerns
        UserMetaModel userMeta = userMetaRepo.findById(userId).get();

        UserAdditionsModel userAdditionsModel = new UserAdditionsModel();
        userAdditionsModel.setUserMeta(userMeta);
        userAdditionsModel.setResourceLimitUsed(0);

        userAdditionsRepo.save(userAdditionsModel);
    }

    @Transactional
    private long getResourceTimeUsed(UUID userId) {
        // SELECT used WHERE uid = 1? AND lastUpdated = today;
        // if used == 0 null = 0
        // Get today's date without time
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date todayStart = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date todayEnd = calendar.getTime();

        UserAdditionsModel userAdditionsModel = userAdditionsRepo.findByUidAndToday(userId,
                new Timestamp(todayStart.getTime()), new Timestamp(todayEnd.getTime()));

        // If not userAdditionsModel was found, it may hint that the last update was not
        // done today, so the user has currently used 0 resources (or the user is not
        // present in db)
        if (userAdditionsModel == null) {
            return 0;
        } else {
            return userAdditionsModel.getResourceLimitUsed();
        }
    }

    public long getResourceLimitLeft(UUID userId) {
        long resourceTimeUsed = getResourceTimeUsed(userId);

        return timeLimitPerDayInSeconds - resourceTimeUsed;
    }

    @Transactional
    public int setResourceLimitLeft(UUID userId, long resourceLimitLeft) {
        // Recalculating the OverallResourceTimeUsed, depending on the resourceLimitLeft
        long resourceTimeUsed = (resourceLimitLeft - timeLimitPerDayInSeconds) * -1;
        int updateCount = userAdditionsRepo.UpdateResourceTimeUsedAndLastChangedAtByUid(userId, resourceTimeUsed,
                Timestamp.from(Instant.now()));

        return updateCount;
    }
}
