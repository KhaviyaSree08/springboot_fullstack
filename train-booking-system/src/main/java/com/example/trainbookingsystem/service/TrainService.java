package com.example.trainbookingsystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.trainbookingsystem.entity.Train;
import com.example.trainbookingsystem.repository.TrainRepository;

@Service
public class TrainService {

    @Autowired
    private TrainRepository trainRepository;

    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }

    public Optional<Train> getTrainById(Long id) {
        return trainRepository.findById(id);
    }

    public Train createTrain(Train train) {
        return trainRepository.save(train);
    }

    public Train updateTrain(Long id, Train trainDetails) {
        Train train = trainRepository.findById(id).orElseThrow();
        train.setName(trainDetails.getName());
        train.setSource(trainDetails.getSource());
        train.setDestination(trainDetails.getDestination());
        train.setBasePrice(trainDetails.getBasePrice());
        train.setDiscountPercentage(trainDetails.getDiscountPercentage());
        return trainRepository.save(train);
    }

    public void deleteTrain(Long id) {
        trainRepository.deleteById(id);
    }
}