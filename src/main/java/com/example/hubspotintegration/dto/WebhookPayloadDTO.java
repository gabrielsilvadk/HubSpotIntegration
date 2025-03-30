package com.example.hubspotintegration.dto;

import java.util.List;

public class WebhookPayloadDTO {
    private List<WebhookEventDTO> events;

    public List<WebhookEventDTO> getEvents() {
        return events;
    }

    public void setEvents(List<WebhookEventDTO> events) {
        this.events = events;
    }

    public static class WebhookEventDTO {
        private String eventType;
        private String objectId;
        private String propertyName;
        private String propertyValue;
        private long timestamp;

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getPropertyName() {
            return propertyName;
        }

        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        public String getPropertyValue() {
            return propertyValue;
        }

        public void setPropertyValue(String propertyValue) {
            this.propertyValue = propertyValue;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
} 