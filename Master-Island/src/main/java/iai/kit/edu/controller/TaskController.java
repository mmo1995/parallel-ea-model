package iai.kit.edu.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class TaskController {
	


    /**
     * Get an ID for the task
     *
     * @throws IOException
     **/
    @RequestMapping(value = "/opt/taskID", method = RequestMethod.GET)
    public String getIDforTask() throws IOException {

       return "1";
    }
    
    @RequestMapping(value = "/opt/{id}/start", method = RequestMethod.POST)
    public String startTask(@PathVariable String id, @RequestBody String configuration) {
        if (!id.equals("0") && Integer.parseInt(id) == 1) {
            return "1";
        } else
            return "-1";
    }
    
}
