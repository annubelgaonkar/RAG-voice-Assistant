package dev.anuradha.voiceragassistant.ingest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AutoIngestRunner implements ApplicationRunner {

    private final IngestionService ingestionService;

    @Value("${app.autoIngest:false}")
    private boolean autoIngest;

    @Override
    public void run(ApplicationArguments args) {
        if (!autoIngest) {
            log.info("Auto-ingest is disabled (app.autoIngest=false)");
            return;
        }
        log.info("Auto-ingest enabled. Ingesting FAQs...");
        int count = ingestionService.ingestClasspathFaqs();
        log.info("Auto-ingest complete. Ingested {} vectors.", count);
    }
}
