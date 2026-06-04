package com.productmanagementServ.merchant.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class NotificationService {

    private final ConcurrentHashMap<Integer, CopyOnWriteArrayList<SseEmitter>> merchantEmitters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CopyOnWriteArrayList<SseEmitter>> customerEmitters = new ConcurrentHashMap<>();

    public SseEmitter subscribeMerchant(Integer merchantId) {
        SseEmitter emitter = new SseEmitter(0L);
        merchantEmitters.computeIfAbsent(merchantId, k -> new CopyOnWriteArrayList<>()).add(emitter);
        Runnable cleanup = () -> removeEmitter(merchantEmitters, merchantId, emitter);
        emitter.onCompletion(cleanup);
        emitter.onTimeout(cleanup);
        emitter.onError(e -> cleanup.run());
        return emitter;
    }

    public SseEmitter subscribeCustomer(String phone) {
        SseEmitter emitter = new SseEmitter(0L);
        customerEmitters.computeIfAbsent(phone, k -> new CopyOnWriteArrayList<>()).add(emitter);
        Runnable cleanup = () -> removeEmitter(customerEmitters, phone, emitter);
        emitter.onCompletion(cleanup);
        emitter.onTimeout(cleanup);
        emitter.onError(e -> cleanup.run());
        return emitter;
    }

    public void notifyMerchant(Integer merchantId, Object payload) {
        CopyOnWriteArrayList<SseEmitter> emitters = merchantEmitters.get(merchantId);
        if (emitters != null) broadcast(emitters, payload);
    }

    public void notifyCustomer(String phone, Object payload) {
        CopyOnWriteArrayList<SseEmitter> emitters = customerEmitters.get(phone);
        if (emitters != null) broadcast(emitters, payload);
    }

    private void broadcast(CopyOnWriteArrayList<SseEmitter> emitters, Object payload) {
        List<SseEmitter> dead = new ArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().data(payload, MediaType.APPLICATION_JSON));
            } catch (IOException e) {
                dead.add(emitter);
            }
        }
        emitters.removeAll(dead);
    }

    private <K> void removeEmitter(ConcurrentHashMap<K, CopyOnWriteArrayList<SseEmitter>> map, K key, SseEmitter emitter) {
        CopyOnWriteArrayList<SseEmitter> list = map.get(key);
        if (list != null) list.remove(emitter);
    }
}
