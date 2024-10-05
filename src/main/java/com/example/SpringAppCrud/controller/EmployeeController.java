package com.example.SpringAppCrud.controller;

import com.example.SpringAppCrud.entity.Employee;
import com.example.SpringAppCrud.service.EmployeeService;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService theEmployeeService) {
        this.employeeService = theEmployeeService;
    }

    @GetMapping("/list")
    public String listEmployees(Model theModel) {
        List<Employee> theEmployees = employeeService.findAll();
        theModel.addAttribute("employees", theEmployees);
        return "employees/list-employees";
    }

    @GetMapping("/addNewEmployee")
    public String addNewEmployee(Model theModel) {
        Employee theEmployee = new Employee();
        theModel.addAttribute("employee", theEmployee);
        return "employees/employee-form";
    }

    @GetMapping("/updateEmployee")
    public String updateEmployee(@RequestParam("employeeId") int theId, Model theModel) {
        Employee theEmployee = employeeService.findById(theId);
        theModel.addAttribute("employee", theEmployee);
        return "employees/employee-form";
    }

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String saveEmployee(@ModelAttribute("employee") Employee theEmployee, @RequestParam("imageFile") MultipartFile imageFile) {
        if (!imageFile.isEmpty()) {
            try {
                theEmployee.setImg1(compressImage(imageFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        employeeService.save(theEmployee);
        return "redirect:/employees/list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("employeeId") int theId) {
        employeeService.deleteById(theId);
        return "redirect:/employees/list";
    }

    private byte[] compressImage(MultipartFile file) throws IOException {
        // Comprimă imaginea la dimensiunea și calitatea dorită
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(originalImage)
                .size(640, 480) // Specifică dimensiunea maximă
                .outputFormat("jpg") // Specifică formatul de ieșire
                .outputQuality(0.5) // Specifică calitatea (0.0 - 1.0)
                .toOutputStream(outputStream);
        return outputStream.toByteArray();
    }

    @GetMapping("/image/{employeeId}")
    public ResponseEntity<byte[]> getEmployeeImage(@PathVariable int employeeId) {
        Employee employee = employeeService.findById(employeeId);
        byte[] imageBytes = employee.getImg1();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }
}
