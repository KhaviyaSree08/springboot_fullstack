package com.example.trainbookingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.trainbookingsystem.entity.Train;
import com.example.trainbookingsystem.service.TrainService;

@Controller
@RequestMapping("/trains")
public class TrainController {

    @Autowired
    private TrainService trainService;

    @GetMapping
    public String getAllTrains(Model model) {
        model.addAttribute("trains", trainService.getAllTrains());
        return "trains/list";
    }

    @GetMapping("/new")
    public String createTrainForm(Model model) {
        model.addAttribute("train", new Train());
        return "trains/form";
    }

    @PostMapping
    public String createTrain(@ModelAttribute Train train) {
        trainService.createTrain(train);
        return "redirect:/trains";
    }

    @GetMapping("/edit/{id}")
    public String editTrainForm(@PathVariable Long id, Model model) {
        model.addAttribute("train", trainService.getTrainById(id).orElseThrow());
        return "trains/form";
    }

    @PostMapping("/update/{id}")
    public String updateTrain(@PathVariable Long id, @ModelAttribute Train train) {
        trainService.updateTrain(id, train);
        return "redirect:/trains";
    }

    @GetMapping("/delete/{id}")
    public String deleteTrain(@PathVariable Long id) {
        trainService.deleteTrain(id);
        return "redirect:/trains";
    }
}