package com.example.trainbookingsystem;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.trainbookingsystem.entity.Train;
import com.example.trainbookingsystem.repository.TrainRepository;
import com.example.trainbookingsystem.service.TrainService;

@ExtendWith(MockitoExtension.class)
public class TrainServiceTest {

    @Mock
    private TrainRepository trainRepository;

    @InjectMocks
    private TrainService trainService;

    private Train train;

    @BeforeEach
    void setUp() {
        train = new Train();
        train.setId(1L);
        train.setName("Express Train");
        train.setSource("City A");
        train.setDestination("City B");
        train.setBasePrice(100.0);
        train.setDiscountPercentage(10.0);
    }

    @Test
    void getAllTrains_ShouldReturnListOfTrains() {
        List<Train> trains = Arrays.asList(train);
        when(trainRepository.findAll()).thenReturn(trains);

        List<Train> result = trainService.getAllTrains();

        assertEquals(1, result.size());
        assertEquals(train.getName(), result.get(0).getName());
        verify(trainRepository, times(1)).findAll();
    }

    @Test
    void getTrainById_WhenTrainExists_ShouldReturnTrain() {
        when(trainRepository.findById(1L)).thenReturn(Optional.of(train));

        Optional<Train> result = trainService.getTrainById(1L);

        assertTrue(result.isPresent());
        assertEquals(train.getName(), result.get().getName());
        verify(trainRepository, times(1)).findById(1L);
    }

    @Test
    void getTrainById_WhenTrainDoesNotExist_ShouldReturnEmpty() {
        when(trainRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Train> result = trainService.getTrainById(1L);

        assertFalse(result.isPresent());
        verify(trainRepository, times(1)).findById(1L);
    }

    @Test
    void createTrain_ShouldSaveAndReturnTrain() {
        when(trainRepository.save(any(Train.class))).thenReturn(train);

        Train result = trainService.createTrain(train);

        assertNotNull(result);
        assertEquals(train.getName(), result.getName());
        verify(trainRepository, times(1)).save(train);
    }

    @Test
    void updateTrain_WhenTrainExists_ShouldUpdateAndReturnTrain() {
        Train updatedTrain = new Train();
        updatedTrain.setId(1L);
        updatedTrain.setName("Express Train Updated");
        updatedTrain.setSource("City A");
        updatedTrain.setDestination("City B");
        updatedTrain.setBasePrice(120.0);
        updatedTrain.setDiscountPercentage(15.0);

        when(trainRepository.findById(1L)).thenReturn(Optional.of(train));
        when(trainRepository.save(any(Train.class))).thenReturn(updatedTrain);

        Train result = trainService.updateTrain(1L, updatedTrain);

        assertEquals(updatedTrain.getName(), result.getName());
        assertEquals(updatedTrain.getSource(), result.getSource());
        assertEquals(updatedTrain.getDestination(), result.getDestination());
        assertEquals(updatedTrain.getBasePrice(), result.getBasePrice());
        assertEquals(updatedTrain.getDiscountPercentage(), result.getDiscountPercentage());
        verify(trainRepository, times(1)).findById(1L);
        verify(trainRepository, times(1)).save(updatedTrain);
    }

    @Test
    void updateTrain_WhenTrainDoesNotExist_ShouldThrowException() {
        when(trainRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> trainService.updateTrain(1L, train));
        verify(trainRepository, times(1)).findById(1L);
        verify(trainRepository, never()).save(any(Train.class));
    }

    @Test
    void deleteTrain_ShouldDeleteTrain() {
        doNothing().when(trainRepository).deleteById(1L);

        trainService.deleteTrain(1L);

        verify(trainRepository, times(1)).deleteById(1L);
    }
}
