package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.OrderNotFoundException;
import exceptions.ScheduleNotFoundException;
import lombok.NonNull;
import models.OrderData;
import models.ScheduleData;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "order")
public class CinemaOrderController {
    private static final String ordersPath = "C:\\ORDERSDATA.TXT";
    private static final String schedulePath = "C:\\SCHEDULE.TXT";
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public OrderData getOrderByID(@PathVariable @NonNull int id) throws OrderNotFoundException {

        try(BufferedReader reader = new BufferedReader(new FileReader(ordersPath))){
            ObjectMapper objectMapper = new ObjectMapper();
            String line = reader.readLine();
            OrderData order;
            while (line != null){
                order = objectMapper.readValue(line, OrderData.class);
                if(order.getId() == id){
                    return order;
                }
                line = reader.readLine();
            }
        } catch (IOException e){}
        throw new OrderNotFoundException();
    }

    @RequestMapping(value = "/schedule", method = RequestMethod.GET)
    public ArrayList<ScheduleData> getSchedule() throws ScheduleNotFoundException {
        ArrayList<ScheduleData> schedule = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(schedulePath))){
            ObjectMapper objectMapper = new ObjectMapper();
            String line = reader.readLine();
            while (line != null) {
                ScheduleData item = objectMapper.readValue(line, ScheduleData.class);
                schedule.add(item);
                line = reader.readLine();
            }
        } catch (IOException e){}
        if(schedule.isEmpty()) {
            throw new ScheduleNotFoundException();
        }
        return schedule;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String deleteOrder(@PathVariable @NonNull int id) throws OrderNotFoundException {
        ArrayList<String> newOrderList = new ArrayList<>();
        String response = null;
        try(BufferedReader reader = new BufferedReader(new FileReader(ordersPath))){
            ObjectMapper objectMapper = new ObjectMapper();
            String line = reader.readLine();
            OrderData order;
            while (line != null){
                order = objectMapper.readValue(line, OrderData.class);
                if(order.getId() == id){
                    response = "order sucsesfully deleted";
                } else {
                    newOrderList.add(line);
                }
                line = reader.readLine();
            }
        } catch (IOException e){}
        if (response != null){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ordersPath))){
            for(String newOrder:newOrderList){
                writer.write(newOrder);
                writer.newLine();
            }
        } catch (IOException e){}
        } else {
            throw new OrderNotFoundException();
        }
        return response;
    }

    @RequestMapping(method = RequestMethod.POST)
    public OrderData addOrder(@RequestParam @NonNull int movieID, @RequestParam @NonNull int number){
        OrderData newOrder = null;
        try(BufferedReader reader = new BufferedReader(new FileReader(schedulePath));
            BufferedReader orderReader = new BufferedReader(new FileReader(ordersPath));
            BufferedWriter orderWriter = new BufferedWriter(new FileWriter(ordersPath, true))) {
            ObjectMapper objectMapper = new ObjectMapper();
            ScheduleData movie = null;
            String line = reader.readLine();
            while (line != null){
                movie = objectMapper.readValue(line, ScheduleData.class);
                if (movie.getId() == movieID) break;
            }
            int orderID = 1;
            String lastOrder = null;
            String order = orderReader.readLine();
            while(order != null){
                lastOrder = order;
                order = orderReader.readLine();
            }
            if (lastOrder != null) {
                orderID = objectMapper.readValue(lastOrder, OrderData.class).getId()+1;
            }
            newOrder = new OrderData(orderID, number, movie);
            if (orderID != 1) {
                orderWriter.newLine();
            }
            objectMapper.writeValue(orderWriter, newOrder);
        } catch(IOException e){}
        return newOrder;
    }
}
