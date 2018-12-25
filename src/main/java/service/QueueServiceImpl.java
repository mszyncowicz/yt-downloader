package service;

import api.Observable;
import api.Observer;
import api.QueueService;
import downloader.LogObserver;
import downloader.MediaToolExecutor;
import model.Record;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Class is using MediatoolExecutor with YoutubeDLCommander to download links in FIFO order.
 */

public class QueueServiceImpl implements QueueService<Record> {

    private Deque<Record> deque;

    public QueueServiceImpl() {
        deque = new LinkedList<>();
    }

    @Override
    public int size() {
        return deque.size();
    }

    @Override
    public void enqueue(Record record) {
        deque.addLast(record);
    }

    @Override
    public Record dequeue() {
        return deque.pop();
    }



}
