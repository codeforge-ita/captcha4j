package com.aria.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TokensPurgeTask {


    @Scheduled(cron = "${purge.cron.expression}")
    public void purgeExpired() {

    }
}
