package com.main10.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableBatchProcessing
public class JobScheduleConfig {
    @Autowired public JobBuilderFactory jobBuilderFactory;
    @Autowired public StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job ExampleJob(){
        return jobBuilderFactory.get("exampleJob")
                .start(Step())
                .build();
    }

    @Bean
    public Step Step() {
        return stepBuilderFactory.get("step")
                .tasklet(((contribution, chunkContext) -> {
                    log.info("STEP--");
                    return RepeatStatus.FINISHED;
                })).build();
    }

}
