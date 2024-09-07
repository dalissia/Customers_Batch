package com.example.CustomerBatch.config;

import com.example.CustomerBatch.dto.CustomerDTO;
import com.example.CustomerBatch.entities.Customer;
import com.example.CustomerBatch.mappers.CustomerMapper;
import com.example.CustomerBatch.repository.CustomerRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;

import static com.fasterxml.jackson.databind.util.ClassUtil.name;

public class SpringBatchConfig {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomerMapper customerMapper;

    @Bean
    public Job job(JobRepository jobRepository, Step step){
        return new JobBuilder("job1",jobRepository)
                 .start(step)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) throws IOException{
        return new StepBuilder("step1", jobRepository)
                .<CustomerDTO, Customer> chunk (5, platformTransactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .transactionManager(platformTransactionManager)
                .build();


    }
    @Bean
    public ItemReader<CustomerDTO> reader(){
        return new FlatFileItemReaderBuilder<CustomerDTO>()
                .name("csvReader")
                .resource(new ClassPathResource("customers.csv"))
                .comments("..","//","#")
                .delimited()
                .names("id","firstName","lastName","email","gender","contactNo","country","dob")
                .linesToSkip(1)
                .targetType(CustomerDTO.class)
                .build();
    }
    public ItemReader<CustomerDTO> customerItemReader(){
        return new CustomerItemReader.Builder()
                .filename("customers.csv")
                .linesToSkip(1)
                .comments("..","#","//")
                .delimiter(",")
                .build();
    }

@Bean
    public ItemProcessor<CustomerDTO, Customer> processor() {
        return customerDTO -> {
            return customerMapper.from(customerDTO);
        };
    }

@Bean
    public ItemWriter  writer() {
        return chunk-> customerRepository.saveAll(chunk.getItems());

    }

    @Bean
    public TaskExecutor taskExecutor(){
        SimpleAsyncTaskExecutor taskExecutor=new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(10);
        return taskExecutor;
    }

}
