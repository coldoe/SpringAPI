package com.kamiltest.demo.doa.controller;

import com.kamiltest.demo.doa.model.Worker;
import com.kamiltest.demo.manager.WorkerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/api/worker/")
public class WorkerController {
    private WorkerManager workerManager;

    @Autowired
    public WorkerController(WorkerManager workerManager) {
        this.workerManager = workerManager;
    }

    @GetMapping("/getallworkers")
    public String getAllWorkersGetMethod(Model model)
    {
        model.addAttribute("workers", this.workerManager.findAll());
        return "Worker/getAllWorkers";
    }

    @GetMapping("/addworker")
    public String addWorkerGetMethod(Model model)
    {
        model.addAttribute("worker", new Worker());
        return "Worker/addWorker";
    }

    @PostMapping("/addworker")
    public String addWorkerPostMethod(@Valid @ModelAttribute("worker")Worker worker,
                                      BindingResult result){
        if(result.hasErrors())
        {
            return "Worker/addWorker";
        }
        else{
//            Calendar today = Calendar.getInstance();
            worker.setUtilCalendar(new Date());
            this.workerManager.save(worker);
            return "redirect:/api/worker/getallworkers";
        }
    }
    @GetMapping("/updateworker/{id}")
    public String updateWorkerGetMethod(@PathVariable Long id, Model model)
    {
        Optional<Worker> workerToUpdate = this.workerManager.findById(id);
        if(workerToUpdate.isPresent())
        {
            model.addAttribute("workerToUpdate", workerToUpdate.get());
            return "Worker/updateWorker";
        }
        return "redirect:/api/worker/getallworkers";
    }

    @PutMapping("/updateworker/{id}")
    public String updateWotkerPutMethod(@Valid @ModelAttribute("workerToUpdate")Worker worker,
                                        BindingResult result)
    {
        if(result.hasErrors())
        {
            return "Worker/updateWorker";
        }
        this.workerManager.save(worker);
        return "redirect:/api/worker/getallworkers";
    }

    @DeleteMapping("/deleteworker/{id}")
    public String deleteWorker(@PathVariable Long id)
    {
        Optional<Worker> workerToDelete = this.workerManager.findById(id);
        if(workerToDelete.isPresent())
        {
            //maybe check that he has no orders?
            //no cascade i mean?
            this.workerManager.delete(id);
        }
        return "redirect:/api/worker/getallworkers";
    }


}
