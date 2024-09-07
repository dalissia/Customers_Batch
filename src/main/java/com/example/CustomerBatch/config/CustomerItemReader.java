package com.example.CustomerBatch.config;

import com.example.CustomerBatch.dto.CustomerDTO;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class CustomerItemReader  implements ItemReader<CustomerDTO> {
    String fileName;
    int linesToSkip;
    String delimiter;
    BufferedReader bufferedReader;
    String[] comments = new String[0];
    boolean finished = false;

    CustomerItemReader(String fineName, int linesToSkip, String delimiter, String[] comments) throws IOException {
        this.fileName = fineName;
        this.linesToSkip = linesToSkip;
        this.delimiter = delimiter;
        this.comments = comments;
    }

    public CustomerItemReader() {
    }


    @Override
    public CustomerDTO read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if(bufferedReader==null){
            Path path = Paths.get(new ClassPathResource(fileName).getFile().getPath());
            bufferedReader = Files.newBufferedReader(path);
            for (int i = 0; i < linesToSkip; i++ ){
                bufferedReader.readLine();
            }

        }
        CustomerDTO customerDto = null;
        String line = null;
        boolean readNextLine = true;
        while (readNextLine == true){
            line = bufferedReader.readLine();
            if (line == null){
                bufferedReader.close();
                return null;
            }
            for (String comment : comments) {
                if (line.startsWith(comment)) {
                    readNextLine = true;
                    break;
                } else {
                    readNextLine = false;
                }
            }
        }

        String[] items = line.split(delimiter);
        System.out.println(Arrays.asList(items));
        customerDto = new CustomerDTO();
        customerDto.setId(Long.parseLong(items[0]));
        customerDto.setFirstName(items[1]);
        customerDto.setLastName(items[2]);
        customerDto.setEmail(items[3]);
        customerDto.setGender(items[4]);
        customerDto.setContactNo(items[5]);
        customerDto.setCountry(items[6]);
        customerDto.setDob(items[7]);
        return customerDto;
    }
    public static class Builder{
        private CustomerItemReader customItemReader=new CustomerItemReader();

        public Builder filename(String fileName){
            this.customItemReader.fileName=fileName;
            return this;
        }
        public Builder linesToSkip(int linesToSkip){
            this.customItemReader.linesToSkip=linesToSkip;
            return this;
        }
        public Builder comments(String... comments){
            this.customItemReader.comments=comments;
            return this;
        }
        public Builder delimiter(String delimiter){
            this.customItemReader.delimiter=delimiter;
            return this;
        }
        public CustomerItemReader build(){
            return this.customItemReader;
        }
    }
}