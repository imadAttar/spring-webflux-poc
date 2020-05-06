package com.imad.poc.controller;

import com.imad.poc.model.Employee;
import com.imad.poc.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public void create(@Valid @RequestBody Employee e) {
        employeeService.create(e);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mono<Employee>> findById(@PathVariable("id") Integer id) {
        Mono<Employee> e = employeeService.findById(id);
        HttpStatus status = e != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(e, status);
    }

    @GetMapping(value = "/{name}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Employee> findByName(@PathVariable("name") String name) {
        return employeeService.findByName(name);
    }

    @GetMapping
    public Flux<Employee> findAll() {
        return employeeService.findAll();
    }

    @GetMapping("/callme")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void callMe() {
        for (int i = 0; i < 1000000; i++) {
            Employee emp = new Employee();
            emp.setId(i);
            emp.setName("user_" + i);
            emp.setSalary(100 + i);
            employeeService.create(emp);
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<Employee> update(@RequestBody Employee e) {
        return employeeService.update(e);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Integer id) {
        employeeService.delete(id).subscribe();
    }


}
