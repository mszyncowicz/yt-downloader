package api;

import downloader.LogObserver;
import model.Record;

import java.util.List;
import java.util.Optional;

public interface QueueService<T> {
    int size();

    void enqueue(T record);

    T dequeue();
}
