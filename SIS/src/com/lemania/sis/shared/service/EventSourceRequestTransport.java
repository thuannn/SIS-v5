package com.lemania.sis.shared.service;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.gwt.client.DefaultRequestTransport;
import com.google.web.bindery.requestfactory.shared.RequestTransport;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.lemania.sis.client.event.ActionCompletedEvent;
import com.lemania.sis.client.event.ActionInProgressEvent;

public class EventSourceRequestTransport implements RequestTransport {
    private final EventBus eventBus;
    private final RequestTransport wrapped;
    
    /**/
    public EventSourceRequestTransport(EventBus eventBus) {
        this(eventBus, new DefaultRequestTransport());        
    }

    
    /**/
    public EventSourceRequestTransport(EventBus eventBus, RequestTransport wrapped) {
        this.eventBus = eventBus;
        this.wrapped = wrapped;
    }

    
    /**/
    public void send(String payload, final TransportReceiver receiver) {
    	//
        TransportReceiver myReceiver = new TransportReceiver() {
            @Override
            public void onTransportSuccess(String payload) {
                try {
                    receiver.onTransportSuccess(payload);
                } finally {
                    eventBus.fireEvent(new ActionCompletedEvent());
                }
            }

            @Override
            public void onTransportFailure(ServerFailure failure) {
                try {
                    receiver.onTransportFailure(failure);
                } finally {
                    eventBus.fireEvent(new ActionCompletedEvent());
                }
            }
        };

        try {
        	//        	
        	wrapped.send(payload, myReceiver);
        } finally {
        	//
            eventBus.fireEvent(new ActionInProgressEvent());
        }
    }

}