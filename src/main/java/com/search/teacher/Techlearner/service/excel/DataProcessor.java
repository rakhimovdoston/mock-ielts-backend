package com.search.teacher.Techlearner.service.excel;

import org.apache.poi.ss.usermodel.Row;

import java.util.concurrent.BlockingQueue;

public class DataProcessor implements Runnable {
    private BlockingQueue<Row> queue;
    private RedisCache redisCache;
    private RabbitMQClient rabbitMQClient;

    public DataProcessor(BlockingQueue<Row> queue, RedisCache redisCache, RabbitMQClient rabbitMQClient) {
        this.queue = queue;
        this.redisCache = redisCache;
        this.rabbitMQClient = rabbitMQClient;
    }

    @Override
    public void run() {
        try {
            while (!queue.isEmpty()) {
                Row row = queue.take(); // Retrieve row from the queue
                // Process the row data
                // Example: Send data to Redis cache
                redisCache.putData(row);
                // Example: Send data to RabbitMQ for further processing
                rabbitMQClient.sendMessage(row);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}



